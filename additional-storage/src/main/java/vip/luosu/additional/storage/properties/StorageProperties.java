package vip.luosu.additional.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("luc.storage")
public class StorageProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 存储根目录
     */
    private String storagePath;

    /**
     * 优先使用存储类型
     */
    private String primaryType;

    /**
     * 是否清理无效的存储记录
     */
    private boolean cleanInvalid;

    /**
     * 清理无效记录定时调度表达式
     */
    private String cleanCron = "0 1 0 * * ?";

    @NestedConfigurationProperty
    private S3StorageProperties s3 = new S3StorageProperties();

    @NestedConfigurationProperty
    private ServerStorageProperties server = new ServerStorageProperties();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public boolean isCleanInvalid() {
        return cleanInvalid;
    }

    public void setCleanInvalid(boolean cleanInvalid) {
        this.cleanInvalid = cleanInvalid;
    }

    public String getCleanCron() {
        return cleanCron;
    }

    public void setCleanCron(String cleanCron) {
        this.cleanCron = cleanCron;
    }

    public S3StorageProperties getS3() {
        return s3;
    }

    public void setS3(S3StorageProperties s3) {
        this.s3 = s3;
    }

    public ServerStorageProperties getServer() {
        return server;
    }

    public void setServer(ServerStorageProperties server) {
        this.server = server;
    }
}
