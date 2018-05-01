package util;

import com.google.gson.GsonBuilder;

public class JsonUtil {
    public static String toJson(Object obj) {
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
        return jsonString;
    }
}
