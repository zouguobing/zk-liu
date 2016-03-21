package com.bt.liu;

/**
 * Created by binglove on 16/3/17.
 */
public class ZkMain {

    public static void main(String[] arges) throws Exception {

//        ZkClient zkClient = new ZkClient("127.0.0.1:21181");

        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration("127.0.0.1:21181","finance-activity","finance","test");
        propertiesConfiguration.afterPropertiesSet();

        String res = propertiesConfiguration.getString("activity.invite");

        System.out.println("...."+res);

        System.in.read();

    }
}
