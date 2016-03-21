package com.bt.liu;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

/**
 * Created by binglove on 16/3/17.
 */
public class KryoSerialization {

    private static final KryoFactory factory = new KryoFactory() {
        public Kryo create () {
            return new Kryo();
        }
    };

    //softReference cache in memory, when jvm gc'ed will clean them
    private static final KryoPool pool = new KryoPool.Builder(factory).softReferences().build();


    public static byte[] serialize(Object serObj) {
        byte[] buffer = new byte[2048];
        Kryo kryo =  pool.borrow();
        try(Output output = new Output(buffer,-1)) {
            kryo.writeObject(output,serObj);
            return output.toBytes();
        } finally {
            pool.release(kryo);
        }
    }


    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if(bytes == null) return null;
        Kryo kryo =  pool.borrow();
        try(Input input = new Input(bytes)) {
            return kryo.readObject(input,clazz);
        } finally {
            pool.release(kryo);
        }
    }
}
