package vip.luosu.additional.storage.service;

import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.entity.UploadInfo;

import java.io.InputStream;

public interface StorageService {

    String getType();

    boolean support(String type);

    InputStream getInputStream(String source);

    StorageFile upload(InputStream inputStream, String originalFilename, UploadInfo uploadInfo);

    /**
     * 上传文件流，需要手动关闭
     *
     * @param inputStream 文件流
     * @param source      保存路径
     */
    void upload(InputStream inputStream, String source);

    void copy(String source, String destination);

    void move(String source, String destination);

    void delete(String source);

    boolean isExists(String source);
}
