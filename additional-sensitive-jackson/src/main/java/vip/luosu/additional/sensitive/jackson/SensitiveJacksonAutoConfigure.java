package vip.luosu.additional.sensitive.jackson;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

@Configuration
@EnableConfigurationProperties(SensitiveJacksonProperties.class)
@ConditionalOnProperty(prefix = "luc.sensitive.jackson", name = "enable", havingValue = "true")
public class SensitiveJacksonAutoConfigure {

    @Bean
    public JacksonModule sensitiveModule() {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new SensitiveValueSerializerModifier());
        return module;
    }
}
