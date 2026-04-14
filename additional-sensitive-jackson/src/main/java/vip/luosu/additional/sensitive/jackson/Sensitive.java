package vip.luosu.additional.sensitive.jackson;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    SensitiveType type();

    /**
     * 前缀保留位数
     */
    int prefixKeep() default 0;

    /**
     * 后缀保留位数
     */
    int suffixKeep() default 0;

    /**
     * 掩码字符
     */
    char maskChar() default '*';
}
