package vip.luosu.additional.storage.controller;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.luosu.additional.common.BizException;
import vip.luosu.additional.common.Result;
import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.service.StorageService;

import java.io.InputStream;

/**
 * 附件管理
 */
@RestController
@RequestMapping("/storageFile")
@ConditionalOnProperty(prefix = "luc.storage", name = "enable", havingValue = "true")
public class StorageController {

    private static final Logger log = LoggerFactory.getLogger(StorageController.class);

    @Resource
    private StorageService storageService;

    /**
     * 上传附件
     *
     * @param file 附件
     * @return 附件信息
     */
    @PostMapping("/upload")
    public Result<StorageFile> upload(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return Result.success(storageService.upload(inputStream, file.getOriginalFilename(), null));
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new BizException("上传失败");
        }
    }
}
