package util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import log.LogUtil;

import java.util.logging.Level;

public class SerializeUtil {

    public static Object deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        Object rst = new Kryo().readClassAndObject(input);
        input.close();

        LogUtil.Log(Level.INFO, "Deserialize");
        return rst;
    }

    public static byte[] serialize(Object obj) {
        Output output = new Output(40960, -1);
        new Kryo().writeClassAndObject(output, obj);
        byte[] bytes = output.toBytes();
        output.close();

        LogUtil.Log(Level.INFO, "Serialize");
        return bytes;
    }
}
