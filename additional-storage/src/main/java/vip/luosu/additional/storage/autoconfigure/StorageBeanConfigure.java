package vip.luosu.additional.storage.autoconfigure;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class StorageBeanConfigure implements BeanFactoryPostProcessor, EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(StorageBeanConfigure.class);

    private Environment environment;

    @Override
    @NullMarked
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 1. 读取配置文件中指定 Primary 的 Bean 名称
        String primaryBeanName = StorageNameFactories.getBeanName(environment.getProperty("luc.storage.primary-type"));

        if (primaryBeanName == null) {
            return;
        }

        // 2. 查找并设置 Primary 标志
        try {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(primaryBeanName);
            // 🎯 核心步骤：动态设置 primary 属性为 true
            beanDefinition.setPrimary(true);
            log.info("storage primary type Bean is [" + primaryBeanName + "]");
        } catch (NoSuchBeanDefinitionException e) {
            // 避免因 Bean 不存在而导致应用启动失败
            log.error("storage primary type [" + primaryBeanName + "] no such bean");
        }
    }

    @Override
    @NullMarked
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
