package vip.luosu.additional.storage.service.impl;

import com.github.f4b6a3.uuid.alt.GUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.luosu.additional.common.BizException;
import vip.luosu.additional.common.SizeCountingInputStream;
import vip.luosu.additional.common.util.DateUtils;
import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.entity.UploadInfo;
import vip.luosu.additional.storage.properties.StorageProperties;
import vip.luosu.additional.storage.service.StorageFileService;
import vip.luosu.additional.storage.service.StorageService;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public abstract class BaseStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(BaseStorageService.class);

    protected final StorageProperties storageProperties;
    protected final StorageFileService storageFileService;

    protected BaseStorageService(StorageProperties storageProperties, StorageFileService storageFileService) {
        this.storageProperties = storageProperties;
        this.storageFileService = storageFileService;
    }

    @Override
    public boolean support(String type) {
        return getType().equals(type);
    }

    @Override
    public StorageFile upload(InputStream inputStream, String originalFilename, UploadInfo uploadInfo) {
        LocalDateTime now = LocalDateTime.now();
        String fileId = GUID.v7().toString();
        String saveFilename = fileId + "." + FilenameUtils.getExtension(originalFilename);
        String bindType = "";
        if (null != uploadInfo && StringUtils.isNotBlank(uploadInfo.getBindType())) {
            bindType = uploadInfo.getBindType() + "/";
        }
        String filepath = bindType + now.toLocalDate().format(DateUtils.DATE_FORMATTER) + "/" + saveFilename;
        String path = storageProperties.getStoragePath();
        if (StringUtils.isNotBlank(path) && !path.endsWith("/")) {
            path = path + "/";
        }
        String targetPath = path + filepath;
        SizeCountingInputStream sizeStream = new SizeCountingInputStream(inputStream);
        try {
            upload(sizeStream, targetPath);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BizException("上传文件失败");
        }
        StorageFile storageFile = new StorageFile();
        storageFile.setStorageType(getType());
        storageFile.setFileId(fileId);
        storageFile.setFileSize(String.valueOf(sizeStream.getSize()));
        storageFile.setFilename(originalFilename);
        storageFile.setFilePath(targetPath);
        storageFile.setPreviewUrl("/source/preview/" + saveFilename);
        storageFile.setDownloadUrl("/source/download/" + saveFilename + "?filename=" +
                URLEncoder.encode(originalFilename == null ? saveFilename : originalFilename, StandardCharsets.UTF_8));
        storageFile.setCreateTime(now);
        storageFile.setUpdateTime(now);
        if (null !=  uploadInfo) {
            storageFile.setOperatorId(uploadInfo.getOperatorId());
            storageFile.setOperatorName(uploadInfo.getOperatorName());
            storageFile.setBindId(uploadInfo.getBindId());
            storageFile.setBindType(uploadInfo.getBindType());
        }
        storageFileService.save(storageFile);
        return storageFile;
    }
}
