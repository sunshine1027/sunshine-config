package com.sunshine1027.config.core;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;


/**
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
public class SunshineZookeeperClient {

    private static ZooKeeper zooKeeper;

    private static ZooKeeper getZooKeeperInstance() {
        if (zooKeeper == null) {
            try {
                zooKeeper = new ZooKeeper(BaseConfig.zkServers, 20000, new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
                        try {
                            // session expire, close old and create new
                            if (watchedEvent.getState() == Event.KeeperState.Expired) {
                                zooKeeper.close();
                                zooKeeper = null;
                                getZooKeeperInstance();
                            }
                            String path = watchedEvent.getPath();
                            String key = pathToKey(path);
                            if (key != null) {
                                //开启监听
                                zooKeeper.exists(path, true);
                                if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                                    SunshineCache.deleteCache(key);
                                } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                                    String data = getValueFromZookeeper(key);
                                    SunshineCache.updateCache(key, data);
                                }
                            }
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //第一次初始化zk的时候，刷新所有数据到cache
                freshAllDataToCache();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return zooKeeper;
    }


    private static String pathToKey(String path) {
        if (path == null || path.length() < BaseConfig.basePath.length())
            return null;
        return path.substring(BaseConfig.basePath.length() + 1, path.length());
    }

    private static String keyToPath(String key) {
        return BaseConfig.basePath + "/" + key;
    }

    /**
     * 如果节点不存在，循环创建
     *
     * @param path
     * @return
     */
    private static Stat createNode(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }

        try {
            Stat stat = getZooKeeperInstance().exists(path, true);
            if (stat == null) {
                //查看是否需要创建父节点
                if (path.lastIndexOf("/") > 0) {
                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Stat parentStat = getZooKeeperInstance().exists(parentPath, true);
                    if (parentStat == null) {
                        createNode(parentPath);
                    }
                }
                // 创建节点
                zooKeeper.create(path, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            return getZooKeeperInstance().exists(path, true);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setValueToZookeeper(String key, String value) {
        String path = keyToPath(key);
        if (StringUtils.isEmpty(path))
            return;
        if (value == null)
            return;
        try {
            Stat stat = getZooKeeperInstance().exists(path, true);
            if (stat == null)
                createNode(path);
            getZooKeeperInstance().setData(path, value.getBytes(), -1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static String getValueFromZookeeper(String key) {
        String path = keyToPath(key);
        if (StringUtils.isEmpty(path))
            return null;
        try {
            Stat stat = getZooKeeperInstance().exists(path, true);
            if (stat == null)
                return null;
            byte[] bytes = getZooKeeperInstance().getData(path, true, null);
            if (bytes != null) {
                return new String(bytes);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void freshAllDataToCache() {
        List<String> childKeys = null;
        try {
            childKeys = getZooKeeperInstance().getChildren(BaseConfig.basePath, true);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (childKeys != null && childKeys.size() > 0) {
            for (String key : childKeys) {
                String data = getValueFromZookeeper(key);
                SunshineCache.updateCache(key, data);
            }
        }
    }
}
