package util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SerializeUtil {

    public final static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
    }

    public static Object deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        Object rst = kryo.readClassAndObject(input);
        input.close();

        return rst;
    }

    public static byte[] serialize(Object obj) {
        Output output = new Output(409600, -1);
        kryo.writeClassAndObject(output, obj);
        byte[] bytes = output.toBytes();
        output.close();

        return bytes;
    }
}
