package vip.luosu.additional.sensitive.jackson;

public final class SensitiveUtil {

    private SensitiveUtil() {
    }

    public static String mask(String value, Sensitive sensitive) {
        if (value == null || value.isBlank()) {
            return value;
        }

        return switch (sensitive.type()) {
            case NAME -> maskName(value, sensitive.maskChar());
            case MOBILE -> maskByRange(value, 3, 4, sensitive.maskChar());
            case ID_CARD -> maskByRange(value, 6, 4, sensitive.maskChar());
            case EMAIL -> maskEmail(value, sensitive.maskChar());
            case ADDRESS -> maskAddress(value, sensitive.maskChar());
            case CUSTOM -> maskByRange(value, sensitive.prefixKeep(), sensitive.suffixKeep(), sensitive.maskChar());
        };
    }

    public static String maskByRange(String value, int prefixKeep, int suffixKeep, char maskChar) {
        if (value == null) {
            return null;
        }
        int len = value.length();
        if (prefixKeep + suffixKeep >= len) {
            return value;
        }

        String sb = value.substring(0, prefixKeep) +
                String.valueOf(maskChar).repeat(Math.max(0, len - prefixKeep - suffixKeep)) +
                value.substring(len - suffixKeep, len);
        return sb;
    }

    public static String maskName(String value, char maskChar) {
        if (value.length() <= 1) {
            return String.valueOf(maskChar);
        }
        return value.charAt(0) + String.valueOf(maskChar).repeat(value.length() - 1);
    }

    public static String maskEmail(String value, char maskChar) {
        int atIndex = value.indexOf('@');
        if (atIndex <= 1) {
            return value;
        }
        String prefix = value.substring(0, 1);
        String domain = value.substring(atIndex);
        return prefix + String.valueOf(maskChar).repeat(atIndex - 1) + domain;
    }

    public static String maskAddress(String value, char maskChar) {
        if (value.length() <= 6) {
            return String.valueOf(maskChar).repeat(value.length());
        }
        return value.substring(0, 6) + String.valueOf(maskChar).repeat(value.length() - 6);
    }
}