package com.bt.liu;

import java.io.IOException;
import java.util.Map;

/**
 * Created by binglove on 16/2/19.
 */
public class ZkMain {

    public static void main(String[] arges) throws IOException {

        Map configMap = new ZkLiuClient("127.0.0.1:21181","finance","dev","").getConfigMap();

        System.out.print("->"+configMap.size());

        System.in.read();

    }

}
