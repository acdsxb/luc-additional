package vip.luosu.additional.storage.autoconfigure;

import vip.luosu.additional.common.BizException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNameFactories {

    private static final Map<String, String> MAPPING = new ConcurrentHashMap<>(16);

    static {
        MAPPING.put("s3", StorageName.S3);
        MAPPING.put("server", StorageName.SERVER);
    }

    public static String getBeanName(String storageType) {
        return MAPPING.get(storageType);
    }

    public static void setBeanName(String storageType, String beanName) {
        if (MAPPING.containsKey(storageType)) {
            throw new BizException("已存在相同类型名称：" +  storageType);
        }
        MAPPING.put(storageType, beanName);
    }
}
