package com.sunshine1027.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 客户端client
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
public class SunshineConfigClient {
    private static Executor executor;

    static {
        executor = Executors.newFixedThreadPool(3);
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

    public static void main(String[] args) {
        SunshineConfigClient configClient = new SunshineConfigClient();
        configClient.setValue("testKey1", "testValue");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(configClient.getValue("testKey1"));
    }

}
