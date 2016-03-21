package com.bt.liu;

import com.bt.liu.dto.LiuClient;
import com.bt.liu.dto.LiuConfig;
import com.bt.liu.listener.ZkLiuChildListener;
import com.bt.liu.listener.ZkLiuDataListener;
import com.github.zkclient.ZkClient;
import com.github.zkclient.exception.ZkTimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by binglove on 16/2/18.
 */
public class ZkLiuClient {

    static Logger logger = LoggerFactory.getLogger(ZkLiuClient.class);

    //根路径
    public static final String ROOT_PATH = "/liu/";

    private ZkClient zkClient;

    private static final ExecutorService reloadExecutorService = Executors
            .newFixedThreadPool(1, new NamedThreadFactory("zkLiu-data-backup", true));


    //配置管理Map
    private Map<String, String> configMap = new ConcurrentHashMap<>();

    //modules
    private final Set<String> moduleSet = new ConcurrentHashSet<>();

    //本地缓存文件
    private File file;

    //配置store & load
    private Properties properties = new Properties();

    //修改计数
    private final AtomicLong lastCacheChanged = new AtomicLong();

    //构建zk
    public ZkLiuClient(final String zkServers, String projectCode, String profile, String modules, String applicationName) {
        try {
            file = new File(System.getProperty("user.home") + "/.liu/" + projectCode + "/" + profile   + "/" + applicationName + ".cache");
            if(! file.exists() && file.getParentFile() != null && ! file.getParentFile().exists()){
                if(! file.getParentFile().mkdirs()){
                    throw new IllegalArgumentException("Invalid liu store file " + file + ", cause: Failed to create directory " + file.getParentFile() + "!");
                }
            }
            //获取本地文件配置数据
            loadProperties();
            //session超时默认30s  连接超时默认10s
            zkClient = new ZkClient(zkServers);
            //关闭连接
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        zkClient.close();
                    } catch (Exception e) {
                        logger.warn("Failed to close zookeeper client " + zkServers + ", cause: " + e.getMessage(), e);
                    }
                }
            });
            init(projectCode, profile, modules, applicationName);
        } catch (ZkTimeoutException | IllegalStateException zkException) {
            logger.warn("connect to zk server exception, load properties from local cache");
            recover();
        } catch (Exception e) {
            throw new RuntimeException("配置数据加载异常", e);
        }
    }

    //初始化...
    private void init(String projectCode, String profile, String modules, String applicationName) {
        logger.info("初始化配置数据[ \"projectCode\":{},\"profile\":{},\"modules\":{}]", projectCode, profile, modules);
        //根路径 + 环境 + 项目编号
        final String parentPath = ROOT_PATH + profile + "/" + projectCode;

        List<String> moduleChildren = null;
        boolean isModule = StringUtils.isNotBlank(modules);

        if (isModule) {
            moduleChildren = new ArrayList<>();
            moduleChildren.addAll(Arrays.asList(modules.split(",")));
        } else {
            moduleChildren = zkClient.getChildren(parentPath);
            zkClient.subscribeChildChanges(parentPath, new ZkLiuChildListener(this, false));
        }
        for (String moduleName : moduleChildren) {
            String modulePath = parentPath + "/" + moduleName;
            //订阅module的子节点变更
            zkClient.subscribeChildChanges(modulePath, new ZkLiuChildListener(this, true));
            List<String> nodeChildren = zkClient.getChildren(modulePath);
            if (!nodeChildren.isEmpty()) {
                for (String nodeName : nodeChildren) {
                    String nodePath = modulePath + "/" + nodeName;
                    zkClient.subscribeDataChanges(nodePath, new ZkLiuDataListener(this));
                    configMap.put(moduleName + "." + nodeName, readValue(nodePath));
                }
            }
            addModule(moduleName);
        }
        coverProperties();
        //记录客户端信息
        zkClient.createEphemeralSequential(ROOT_PATH + "clients/" + applicationName, KryoSerialization.serialize(new LiuClient(applicationName, profile, projectCode, isModule ? modules : "*")));
    }

    /**
     * 读取 配置节点数据
     *
     * @param path
     * @return
     */
    public String readValue(String path) {
        LiuConfig liuConfig = KryoSerialization.deserialize(zkClient.readData(path, true), LiuConfig.class);
        return liuConfig != null ? liuConfig.getValue() : "";
    }


    public void putValue(String key, String value) {
        configMap.put(key,value);
        properties.setProperty(key,value);
        reloadExecutorService.execute(new BackupDataRunnable(lastCacheChanged.incrementAndGet()));
    }


    public boolean containsKey(String key) {
        return configMap.containsKey(key);
    }

    public boolean containsModule(String moduleName) {
        return moduleSet.contains(moduleName);
    }


    public void addModule(String moduleName) {
        moduleSet.add(moduleName);
    }


    public String getValue(String key) {
        return configMap.get(key);
    }

    public void subscribeDataChanges(String nodePath, ZkLiuDataListener zkLiuDataListener) {
        zkClient.subscribeDataChanges(nodePath,zkLiuDataListener);
    }

    public void subscribeChildChanges(String parentPath, ZkLiuChildListener zkLiuChildListener) {
        zkClient.subscribeChildChanges(parentPath,zkLiuChildListener);
    }




    //load 本地缓存文件
    private void loadProperties() {
        if (file != null && file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                properties.load(in);
                if (logger.isInfoEnabled()) {
                    logger.info("Load liu store file " + file + ", data: " + properties);
                }
            } catch (Throwable e) {
                logger.warn("Failed to load liu store file " + file, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }



    //从本地缓存文件恢复配置数据
    private void recover() {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            configMap.put(key,value);
        }
    }

    //初始化覆盖本地缓存配置数据
    private void coverProperties() {
        for(Map.Entry<String,String> entry : configMap.entrySet()) {
            properties.setProperty(entry.getKey(),entry.getValue());
        }
    }


    //异步更新本地缓存文件Runnable
    class BackupDataRunnable implements Runnable {
        private long version;
        private BackupDataRunnable(long version){
            this.version = version;
        }
        public void run() {
            doSaveProperties(version);
        }
    }

    //更新本地缓存文件
    public void doSaveProperties(long version) {
        if(version < lastCacheChanged.get()){
            return;
        }
        if (file == null) {
            return;
        }
        Properties newProperties = new Properties();
        // 保存之前先读取一遍，防止冲突
        InputStream in = null;
        try {
            if (file.exists()) {
                in = new FileInputStream(file);
                newProperties.load(in);
            }
        } catch (Throwable e) {
            logger.warn("Failed to load liu store file, cause: " + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        // 保存
        try {
            newProperties.putAll(properties);
            File lockfile = new File(file.getAbsolutePath() + ".lock");
            if (!lockfile.exists()) {
                lockfile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(lockfile, "rw");
            try {
                FileChannel channel = raf.getChannel();
                try {
                    FileLock lock = channel.tryLock();
                    if (lock == null) {
                        throw new IOException("Can not lock the liu cache file " + file.getAbsolutePath() + ", ignore and retry later, maybe multi java process use the file");
                    }
                    // 保存
                    try {
                        if (! file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream outputFile = new FileOutputStream(file);
                        try {
                            newProperties.store(outputFile, "Liu data Cache");
                        } finally {
                            outputFile.close();
                        }
                    } finally {
                        lock.release();
                    }
                } finally {
                    channel.close();
                }
            } finally {
                raf.close();
            }
        } catch (Throwable e) {
            if (version < lastCacheChanged.get()) {
                return;
            } else {
                reloadExecutorService.execute(new BackupDataRunnable(lastCacheChanged.incrementAndGet()));
            }
            logger.warn("Failed to save liu store file, cause: " + e.getMessage(), e);
        }
    }

}
