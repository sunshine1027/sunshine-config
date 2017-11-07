package com.sunshine1027.config.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 客户端client
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
public class SunshineConfigClient {
    private static Executor executor;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        executor = Executors.newFixedThreadPool(3);
        //初始化zk
        SunshineZookeeperClient.initZookeeper();
    }

    public String getValue(String key) {
        return SunshineCache.getCache(key);
    }

    public void setValue(final String key, final String value) {
        executor.execute(new Runnable() {
            public void run() {
                try {
                    SunshineZookeeperClient.setValueToZookeeper(key, value);
                } catch (Exception e) {
                }
            }
        });
    }

}
