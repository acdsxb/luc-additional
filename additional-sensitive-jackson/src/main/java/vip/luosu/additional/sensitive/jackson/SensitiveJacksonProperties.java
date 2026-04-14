package vip.luosu.additional.sensitive.jackson;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("luc.sensitive.jackson")
public class SensitiveJacksonProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public SensitiveJacksonProperties setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
