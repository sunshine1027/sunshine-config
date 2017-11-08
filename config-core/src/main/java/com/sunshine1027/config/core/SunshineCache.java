package com.sunshine1027.config.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
public class SunshineCache {
    private static ConcurrentHashMap<String, String> cacheKV = new ConcurrentHashMap<String, String>();

    public static ConcurrentHashMap<String, String> getAllKeyValue() {
        return cacheKV;
    }

    /**
     * 获取value，客户端调用
     * @param key
     * @return
     */
    public static String getCache(String key) {
        return cacheKV.get(key);
    }

    /**
     * 删除某key-value，zk client调用
     * @param key
     */
    public static void deleteCache(String key) {
        cacheKV.remove(key);
    }

    /**
     * 更新缓存，zk client 调用
     * @param key
     * @param value
     */
    public static void updateCache(String key, String value) {
        cacheKV.put(key, value);
    }
}
