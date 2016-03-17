package com.bt.liu.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by binglove on 16/3/12.
 */
public class KryoSerialization {

    //Kryo simple pool
    private static ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            return kryo;
        }
    };


    public static byte[] serialize(Object serObj) {
        byte[] buffer = new byte[2048];
        try(Output output = new Output(buffer)) {
            kryos.get().writeObject(output,serObj);
            return output.toBytes();
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try(Input input = new Input(bytes)) {
            return kryos.get().readObject(input,clazz);
        }
    }

}
