package vip.luosu.additional.common.util;

import vip.luosu.additional.common.BizException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FileSizeUtil {

    private FileSizeUtil() {
    }

    /**
     * 转换文件大小
     *
     * @param byteSize 文件大小（单位：Byte）
     * @param format   目标格式（"B"、"KB"、"MB"、"GB"、"TB"）
     * @return 转换后的大小
     * @throws BizException 如果 format 不合法
     */
    public static BigDecimal convertSize(String byteSize, String format) {
        return convertSize(new BigDecimal(byteSize), format);
    }

    /**
     * 转换文件大小
     *
     * @param byteSize 文件大小（单位：Byte）
     * @param format   目标格式（"B"、"KB"、"MB"、"GB"、"TB"）
     * @return 转换后的大小
     * @throws BizException 如果 format 不合法
     */
    public static BigDecimal convertSize(long byteSize, String format) {
        return convertSize(BigDecimal.valueOf(byteSize), format);
    }

    /**
     * 转换文件大小
     *
     * @param byteSize 文件大小（单位：Byte）
     * @param format   目标格式（"B"、"KB"、"MB"、"GB"、"TB"）
     * @return 转换后的大小
     * @throws BizException 如果 format 不合法
     */
    public static BigDecimal convertSize(BigDecimal byteSize, String format) {
        if (byteSize == null || byteSize.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("文件大小不能为负数");
        }

        if (format == null) {
            throw new BizException("格式不能为空");
        }

        BigDecimal unit;

        switch (format.toUpperCase()) {
            case "B":
                return byteSize;
            case "KB":
                unit = BigDecimal.valueOf(1024);
                break;
            case "MB":
                unit = BigDecimal.valueOf(1024 * 1024);
                break;
            case "GB":
                unit = BigDecimal.valueOf(1024 * 1024 * 1024);
                break;
            case "TB":
                unit = BigDecimal.valueOf(1024L * 1024 * 1024 * 1024);
                break;
            default:
                throw new BizException("不支持的格式: " + format + "，支持 B, KB, MB, GB, TB");
        }
        // 保留2位小数，四舍五入
        return byteSize.divide(unit, 2, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    /**
     * 根据大小自动转换合适的单位
     *
     * @param byteSize 文件大小（单位：Byte）
     * @return 格式化后的大小（带单位）
     */
    public static String autoConvertSize(String byteSize) {
        return autoConvertSize(new BigDecimal(byteSize));
    }

    /**
     * 根据大小自动转换合适的单位
     *
     * @param byteSize 文件大小（单位：Byte）
     * @return 格式化后的大小（带单位）
     */
    public static String autoConvertSize(long byteSize) {
        return autoConvertSize(BigDecimal.valueOf(byteSize));
    }

    /**
     * 根据大小自动转换合适的单位
     *
     * @param byteSize 文件大小（单位：Byte）
     * @return 格式化后的大小（带单位）
     */
    public static String autoConvertSize(BigDecimal byteSize) {
        if (null == byteSize || byteSize.compareTo(BigDecimal.ZERO) <= 0) {
            return "0B";
        }

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        BigDecimal unitSize = BigDecimal.valueOf(1024);

        int unitIndex = 0;
        while (byteSize.compareTo(unitSize) >= 0 && unitIndex < units.length - 1) {
            byteSize = byteSize.divide(unitSize, 2, RoundingMode.HALF_UP);
            unitIndex++;
        }

        return byteSize.stripTrailingZeros().toPlainString() + units[unitIndex];
    }
}
