package vip.luosu.additional.storage.service;

import vip.luosu.additional.common.BizException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageFactories {

    private static final Map<String, StorageService> storageFactories = new ConcurrentHashMap<>(16);

    public static StorageService getStorageService(String type) {
        return storageFactories.get(type);
    }

    public static void registerStorageService(String type, StorageService storageService) {
        if (storageFactories.containsKey(type)) {
            throw new BizException("已存在相同类型服务：" + type);
        }
        storageFactories.put(type, storageService);
    }
}
