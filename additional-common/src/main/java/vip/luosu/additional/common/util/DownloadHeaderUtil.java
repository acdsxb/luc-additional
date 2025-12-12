package vip.luosu.additional.common.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DownloadHeaderUtil {

    /**
     * 构建 Content-Disposition 头，支持中文和特殊字符，兼容各类浏览器（Chrome、Firefox、Edge、Safari 等）
     */
    public static String buildContentDispositionHeader(String filename) {
        // 避免注入，过滤双引号
        String safeFileName = filename.replace("\"", "'");

        // RFC 5987 编码格式：UTF-8''<urlencoded>
        String encodedFilename = URLEncoder.encode(safeFileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encodedFilename, encodedFilename);
    }
}
