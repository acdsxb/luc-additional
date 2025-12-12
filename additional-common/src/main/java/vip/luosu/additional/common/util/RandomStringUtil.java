package vip.luosu.additional.common.util;

import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;
import java.util.Objects;

public final class RandomStringUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 数字
     */
    private static final RandomStringGenerator NUMERIC =
            new RandomStringGenerator.Builder()
                    .withinRange('0', '9')
                    .usingRandom(SECURE_RANDOM::nextInt)
                    .get();

    /**
     * 小写字母
     */
    private static final RandomStringGenerator LOWER =
            new RandomStringGenerator.Builder()
                    .withinRange('a', 'z')
                    .usingRandom(SECURE_RANDOM::nextInt)
                    .get();

    /**
     * 大写字母
     */
    private static final RandomStringGenerator UPPER =
            new RandomStringGenerator.Builder()
                    .withinRange('A', 'Z')
                    .usingRandom(SECURE_RANDOM::nextInt)
                    .get();

    /**
     * 字母（大小写）
     */
    private static final RandomStringGenerator ALPHABETIC =
            new RandomStringGenerator.Builder()
                    // 在 'A' 到 'z' 的 ASCII 区间内生成，然后过滤出字母（去掉中间的 [ \ ] ^ _ ` 等符号）
                    .withinRange('A', 'z')
                    .filteredBy(Character::isLetter)
                    .usingRandom(SECURE_RANDOM::nextInt) // nextInt(bound) 签名匹配
                    .get();

    /**
     * 数字 + 字母（大小写）
     */
    private static final RandomStringGenerator ALPHANUMERIC =
            new RandomStringGenerator.Builder()
                    // 在 '0' 到 'z' 的 ASCII 区间内生成，然后过滤出字母或数字
                    .withinRange('0', 'z')
                    .filteredBy(Character::isLetterOrDigit)
                    .usingRandom(SECURE_RANDOM::nextInt)
                    .get();

    private RandomStringUtil() {
    }

    /* ======================= 对外 API（对标 RandomStringUtils） ======================= */

    /**
     * 随机数字
     */
    public static String randomNumeric(int count) {
        return random(count, NUMERIC);
    }

    /**
     * 随机字母（大小写）
     */
    public static String randomAlphabetic(int count) {
        return random(count, ALPHABETIC);
    }

    /**
     * 随机大写字母
     */
    public static String randomUpper(int count) {
        return random(count, UPPER);
    }

    /**
     * 随机小写字母
     */
    public static String randomLower(int count) {
        return random(count, LOWER);
    }

    /**
     * 随机字母+数字 ✅
     */
    public static String randomAlphanumeric(int count) {
        return random(count, ALPHANUMERIC);
    }

    /**
     * 等价 RandomStringUtils.random(count)（ASCII 可打印）
     */
    public static String random(int count) {
        return RandomStringGenerator.builder()
                .withinRange(32, 126)
                .usingRandom(SECURE_RANDOM::nextInt)
                .get()
                .generate(count);
    }

    /**
     * 自定义字符池
     */
    public static String randomFromChars(int count, String chars) {
        Objects.requireNonNull(chars, "chars must not be null");
        if (chars.isEmpty()) {
            throw new IllegalArgumentException("chars must not be empty");
        }

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom(chars.toCharArray())
                .usingRandom(SECURE_RANDOM::nextInt)
                .get();

        return generator.generate(count);
    }

    /* ======================= 内部统一校验 ======================= */

    private static String random(int count, RandomStringGenerator generator) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be > 0");
        }
        return generator.generate(count);
    }
}
