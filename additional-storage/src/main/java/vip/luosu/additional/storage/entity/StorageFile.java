package vip.luosu.additional.storage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 附件信息
 *
 * @TableName storage_file
 */
@TableName(value = "storage_file")
public class StorageFile implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 7304596547149223322L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 绑定数据id
     */
    private String bindId;

    /**
     * 绑定数据类型
     */
    private String bindType;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件名，下载及展示
     */
    private String filename;

    /**
     * 文件存储路径
     */
    @JsonIgnore
    private String filePath;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 存储类型，server：服务器，s3：对象存储
     */
    private String storageType;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 扩展信息
     */
    private String extraInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public StorageFile setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBindId() {
        return bindId;
    }

    public StorageFile setBindId(String bindId) {
        this.bindId = bindId;
        return this;
    }

    public String getBindType() {
        return bindType;
    }

    public StorageFile setBindType(String bindType) {
        this.bindType = bindType;
        return this;
    }

    public String getFileId() {
        return fileId;
    }

    public StorageFile setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public StorageFile setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public StorageFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getFileSize() {
        return fileSize;
    }

    public StorageFile setFileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public StorageFile setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public StorageFile setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getStorageType() {
        return storageType;
    }

    public StorageFile setStorageType(String storageType) {
        this.storageType = storageType;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public StorageFile setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public StorageFile setOperatorName(String operatorName) {
        this.operatorName = operatorName;
        return this;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public StorageFile setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public StorageFile setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public StorageFile setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}