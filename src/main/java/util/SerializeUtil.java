package util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SerializeUtil {

    public static Object deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        Object rst = new Kryo().readClassAndObject(input);
        input.close();

        return rst;
    }

    public static byte[] serialize(Object obj) {
        Output output = new Output(4096, -1);
        new Kryo().writeClassAndObject(output, obj);
        byte[] bytes = output.toBytes();
        output.close();

        return bytes;
    }
}
