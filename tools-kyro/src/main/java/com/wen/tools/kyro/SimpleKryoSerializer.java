package com.wen.tools.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author WChen129
 * @date 2018-07-11
 */
public class SimpleKryoSerializer {
    public final static int defaultPoolSize = 16;
    public final static int buffSize = 4096;
    public final static int maxBuffSize = 65537;

    private final static ThreadLocal<Kryo> tl_basic_kryo = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            return kryo;
        }
    };



    private final static Kryo findKryo() {
        Kryo kryo = tl_basic_kryo.get();
        ClassLoader old= kryo.getClassLoader();
        ClassLoader newLoader=SimpleKryoSerializer.class.getClassLoader ();
        if(!old.equals(newLoader)) {
            kryo.getClassResolver().reset();
            kryo.setClassLoader(SimpleKryoSerializer.class.getClassLoader ());
        }
        return kryo;
    }

    public final static <T> byte[] toBytes(T value){
        return toBytes(findKryo(),value);
    }

    public final static <T> byte[] toBytes(Kryo kryo,T value) {
        Output out = new ByteBufferOutput(4096, 1024 * 1024);
        try {
            return toBytes(kryo,out,value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null){
                out.close();
            }
        }
    }

    public final static <T> byte[] toBytes(Kryo kryo,Output out ,T value) {
        try {
            kryo.writeObject(out, value);
            out.flush();
            byte[] bytes = out.toBytes();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final static <T> T toObject(byte[] bytes,Class<T> tClass) {
        Kryo kryo=findKryo();

        T t=toObject(kryo,bytes,tClass);
        return t;
    }
    public final static <T> T toObject(Kryo kryo,byte[] bytes,Class<T> tClass) {

        try {
            Input input = new ByteBufferInput(bytes);
            T t = (T) kryo.readObject(input,tClass);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
