package com.example.tbichan.syaroescape.opengl.store;

import java.util.HashMap;

/**
 * 保存用クラスです。
 * Created by tbichan on 2017/12/12.
 */

public class StoreManager {

    private static HashMap<String, Object> storeMap = new HashMap<>();

    public static void reset() {
        storeMap.clear();
    }

    public static void save(String key, Object value) {
        storeMap.put(key, value);
    }

    public static void saveString(String key, String value) {
        storeMap.put(key, value);
    }

    public static Object restore(String key) {
        return storeMap.get(key);
    }

    public static String restoreString(String key) {
        Object value = storeMap.get(key);

        if (value == null) return null;

        return value.toString();
    }

    public static Integer restoreInteger(String key) {
        Object value = storeMap.get(key);

        if (value == null) return null;

        return Integer.parseInt(value.toString());
    }

    public static boolean containsKey(String key) {
        return storeMap.containsKey(key);
    }
}
