package vip.luosu.additional.storage.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.luosu.additional.common.BizException;
import vip.luosu.additional.common.util.DownloadHeaderUtil;
import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.service.StorageFactories;
import vip.luosu.additional.storage.service.StorageFileService;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/source")
@ConditionalOnProperty(prefix = "luc.storage", name = "enable", havingValue = "true")
public class SourceController {

    @Resource
    private StorageFileService storageFileService;

    @GetMapping("/preview/**")
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sourcePath = (String) request.getAttribute("sourcePath");
        if (!StringUtils.hasText(sourcePath)) {
            sourcePath = request.getRequestURI()
                    .replace(request.getContextPath() + "/source/preview/", "");
            sourcePath = URLDecoder.decode(sourcePath, StandardCharsets.UTF_8);
        }

        StorageFile storageFile = storageFileService.getByFileId(FilenameUtils.getBaseName(sourcePath));
        if (storageFile == null) {
            throw new BizException("文件不存在");
        }
        String filePath = storageFile.getFilePath();
        long fileSize = Long.parseLong(storageFile.getFileSize());

        // ✅ 1. 推断 Content-Type
        String contentType = Files.probeContentType(Paths.get(filePath));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        // ✅ 2. 强制“预览模式”
        response.setHeader("Content-Disposition", "inline");
        // ✅ 3. 处理 Range（视频拖拽关键）
        String range = request.getHeader("Range");

        try (InputStream inputStream =
                     StorageFactories.getStorageService(storageFile.getStorageType())
                             .getInputStream(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {
            if (!StringUtils.hasText(range)) {
                // ✅ 普通全量读取（图片 / pdf）
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Content-Length", storageFile.getFileSize());
                IOUtils.copy(inputStream, outputStream);
                return;
            }
            // ✅ 4. Range 解析（视频）
            long start, end;
            String[] ranges = range.replace("bytes=", "").split("-");
            start = Long.parseLong(ranges[0]);
            end = (ranges.length > 1 && StringUtils.hasText(ranges[1]))
                    ? Long.parseLong(ranges[1])
                    : fileSize - 1;
            if (end > fileSize - 1) {
                end = fileSize - 1;
            }
            long contentLength = end - start + 1;

            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader(
                    "Content-Range",
                    "bytes " + start + "-" + end + "/" + fileSize
            );
            response.setHeader("Content-Length", String.valueOf(contentLength));
            // ✅ 5. 跳过 start 字节
            inputStream.skip(start);
            // ✅ 6. 限流输出
            IOUtils.copyLarge(inputStream, outputStream, 0, contentLength);
        }
    }

    @GetMapping("/download/**")
    public ResponseEntity<InputStreamResource> download(HttpServletRequest request) {
        String sourcePath = (String) request.getAttribute("sourcePath");
        if (!StringUtils.hasText(sourcePath)) {
            sourcePath = request.getRequestURI().replace(request.getContextPath() + "/source/download/", "");
            sourcePath = URLDecoder.decode(sourcePath, StandardCharsets.UTF_8);
        }
        String filename = StringUtils.getFilename(sourcePath);
        String requestName = Optional.ofNullable(request.getParameter("filename")).orElse("");
        // 使用原名称下载
        if (StringUtils.hasText(requestName)) {
            filename = requestName;
        }

        StorageFile storageFile = storageFileService.getByFileId(FilenameUtils.getBaseName(sourcePath));
        if (storageFile == null) {
            throw new BizException("文件不存在");
        }
        InputStream inputStream = StorageFactories.getStorageService(storageFile.getStorageType()).getInputStream(storageFile.getFilePath());

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", DownloadHeaderUtil.buildContentDispositionHeader(filename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(inputStream));
    }

}
