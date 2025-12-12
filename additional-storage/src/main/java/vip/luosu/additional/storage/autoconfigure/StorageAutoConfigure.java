package vip.luosu.additional.storage.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import vip.luosu.additional.storage.BeforeStorageCleanCondition;
import vip.luosu.additional.storage.InvalidFileCleaner;
import vip.luosu.additional.storage.properties.StorageProperties;
import vip.luosu.additional.storage.service.StorageFileService;
import vip.luosu.additional.storage.service.StorageService;
import vip.luosu.additional.storage.service.impl.S3StorageService;
import vip.luosu.additional.storage.service.impl.ServerStorageService;
import vip.luosu.additional.storage.service.impl.StorageFileServiceImpl;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@ConditionalOnProperty(prefix = "luc.storage", name = "enable", havingValue = "true")
@ComponentScan("vip.luosu.additional.storage.controller")
public class StorageAutoConfigure {

    @Bean
    public static StorageBeanConfigure storageBeanConfigure() {
        return new StorageBeanConfigure();
    }

    @Bean
    public StorageFileService storageFileService() {
        return new StorageFileServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(BeforeStorageCleanCondition.class)
    @ConditionalOnProperty(prefix = "luc.storage", name = "clean-invalid", havingValue = "true")
    public BeforeStorageCleanCondition beforeCleanCondition() {
        return () -> true;
    }

    @Bean(name = "storageFileCleanScheduler", destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "luc.storage", name = "clean-invalid", havingValue = "true")
    public ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("StorageFileClean-");
        scheduler.setPoolSize(1);
        return scheduler;
    }

    @Bean
    @ConditionalOnProperty(prefix = "luc.storage", name = "clean-invalid", havingValue = "true")
    public InvalidFileCleaner invalidFileCleaner() {
        return new InvalidFileCleaner();
    }

    @Bean(StorageName.S3)
    @ConditionalOnProperty(prefix = "luc.storage.s3", name = "enable", havingValue = "true")
    public StorageService s3(StorageProperties storageProperties,
                             StorageFileService storageFileService) {
        return new S3StorageService(storageProperties, storageFileService);
    }

    @Bean(StorageName.SERVER)
    @ConditionalOnProperty(prefix = "luc.storage.server", name = "enable", havingValue = "true")
    public StorageService server(StorageProperties storageProperties,
                                 StorageFileService storageFileService) {
        return new ServerStorageService(storageProperties, storageFileService);
    }
}
