package com.bt.liu;

import com.bt.liu.exception.ConvertRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.NoSuchElementException;

/**
 * Created by binglove on 16/1/29.
 */
public class PropertiesConfiguration implements InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfiguration.class);

    //zk 服务地址列表
    private String zkServers;

    //项目编号
    private String projectCode;

    //项目下模块
    private String modules;

    //环境 dev test pro
    private String profile;

    //应用名称
    private String applicationName;


    private ZkLiuClient zkLiuClient;

    public PropertiesConfiguration(String zkServers, String appName, String projectCode, String profile, String modules) {
        this.modules = modules;
        this.profile = profile;
        this.projectCode = projectCode;
        this.zkServers = zkServers;
        this.applicationName = appName;
    }

    public PropertiesConfiguration(String zkServers, String appName, String projectCode, String profile) {
        this.profile = profile;
        this.projectCode = projectCode;
        this.zkServers = zkServers;
        this.applicationName = appName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        zkLiuClient = new ZkLiuClient(zkServers, projectCode, profile, modules, applicationName);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key, Boolean.valueOf(defaultValue));
    }

    public boolean getBoolean(String key) {
        Boolean b = getBoolean(key, null);
        if (b != null) {
            return b.booleanValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }


    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(value);
        }
    }

    public byte getByte(String key) {
        Byte b = getByte(key, null);
        if (b != null) {
            return b;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    public byte getByte(String key, byte defaultValue) {
        return getByte(key, new Byte(defaultValue));
    }

    public Byte getByte(String key, Byte defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Byte.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to a Byte object", e);
            }
        }
    }

    public double getDouble(String key) {
        Double d = getDouble(key, null);
        if (d != null) {
            return d;
        } else {
            throw new NoSuchElementException('\'' + key
                    + "' doesn't map to an existing object");
        }
    }

    public double getDouble(String key, double defaultValue) {
        return getDouble(key, new Double(defaultValue));
    }

    public Double getDouble(String key, Double defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Double.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to a Double object", e);
            }
        }
    }

    public float getFloat(String key) {
        Float f = getFloat(key, null);
        if (f != null) {
            return f;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    public float getFloat(String key, float defaultValue) {
        return getFloat(key, new Float(defaultValue));
    }

    public Float getFloat(String key, Float defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Float.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to a Float object", e);
            }
        }
    }

    public int getInt(String key) {
        Integer i = getInteger(key, null);
        if (i != null) {
            return i;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    public int getInt(String key, int defaultValue) {
        Integer i = getInteger(key, null);

        if (i == null) {
            return defaultValue;
        }

        return i;
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to an Integer object", e);
            }
        }
    }

    public long getLong(String key) {
        Long l = getLong(key, null);
        if (l != null) {
            return l;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    public long getLong(String key, long defaultValue) {
        return getLong(key, new Long(defaultValue));
    }

    public Long getLong(String key, Long defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Long.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to a Long object", e);
            }
        }
    }

    public short getShort(String key) {
        Short s = getShort(key, null);
        if (s != null) {
            return s;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    public short getShort(String key, short defaultValue) {
        return getShort(key, new Short(defaultValue));
    }

    public Short getShort(String key, Short defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Short.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertRuntimeException('\'' + key + "' doesn't map to a Short object", e);
            }
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }


    public String getString(String key, String defaultValue) {
        String value = getProperty(key);

        return value != null ? value : defaultValue;
    }


    private String getProperty(String key) {
        return zkLiuClient.getValue(key);
    }

}
