package vip.luosu.additional.storage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import vip.luosu.additional.common.BizException;
import vip.luosu.additional.storage.properties.StorageProperties;
import vip.luosu.additional.storage.service.StorageFactories;
import vip.luosu.additional.storage.service.StorageFileService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ServerStorageService extends BaseStorageService implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ServerStorageService.class);

    public ServerStorageService(StorageProperties storageProperties, StorageFileService storageFileService) {
        super(storageProperties, storageFileService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StorageFactories.registerStorageService(getType(), this);
    }

    @Override
    public String getType() {
        return "server";
    }

    @Override
    public InputStream getInputStream(String source) {
        try {
            return Files.newInputStream(Paths.get(source));
        } catch (IOException e) {
            log.error("文件不存在", e);
            throw new BizException("文件不存在");
        }
    }

    @Override
    public void upload(InputStream inputStream, String source) {
        try {
            Path targetPath = Paths.get(source);
            Path parent = targetPath.getParent();
            if (Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BizException("上传文件失败");
        }
    }

    @Override
    public void copy(String source, String destination) {
        try {
            Path s = Path.of(source);
            Path t = Path.of(destination);
            if (Files.notExists(t.getParent())) {
                Files.createDirectories(t.getParent());
            }
            Files.copy(s, t, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            log.error("复制文件失败", e);
            throw new BizException("复制文件失败");
        }
    }

    @Override
    public void move(String source, String destination) {
        try {
            Path s = Path.of(source);
            Path t = Path.of(destination);
            if (Files.notExists(t.getParent())) {
                Files.createDirectories(t.getParent());
            }
            Files.move(s, t, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            log.error("移动文件失败", e);
            throw new BizException("移动文件失败");
        }
    }

    @Override
    public void delete(String source) {
        try {
            Files.deleteIfExists(Paths.get(source));
        } catch (IOException e) {
            log.error("删除文件失败", e);
            throw new BizException("删除文件失败");
        }
    }

    @Override
    public boolean isExists(String source) {
        return Files.exists(Paths.get(source));
    }
}
