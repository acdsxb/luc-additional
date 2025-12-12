package vip.luosu.additional.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.properties.StorageProperties;
import vip.luosu.additional.storage.service.StorageFactories;
import vip.luosu.additional.storage.service.StorageFileService;
import vip.luosu.additional.storage.service.StorageService;

import java.util.List;

public class InvalidFileCleaner implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(InvalidFileCleaner.class);
    @Resource
    private StorageProperties storageProperties;
    @Resource
    private BeforeStorageCleanCondition beforeCleanCondition;
    @Resource
    private ThreadPoolTaskScheduler storageFileCleanScheduler;
    @Resource
    private StorageFileService storageFileService;


    @Override
    public void afterPropertiesSet() throws Exception {
        storageFileCleanScheduler.schedule(this::execute, new CronTrigger(storageProperties.getCleanCron()));
    }

    public void execute() {
        boolean aContinue = beforeCleanCondition.isContinue();
        if (!aContinue) {
            log.info("cleaner is skipped");
            return;
        }
        log.info("cleaner is scheduled");
        long page = 1;
        long pageSize = 100;

        LambdaQueryWrapper<StorageFile> wrapper = Wrappers.<StorageFile>lambdaQuery().isNull(StorageFile::getBindId);
        while (true) {
            Page<StorageFile> p = new Page<>(page, pageSize);
            Page<StorageFile> result = storageFileService.page(p, wrapper);
            List<StorageFile> records = result.getRecords();
            if (records.isEmpty()) {
                break;
            }

            for (StorageFile record : records) {
                StorageService storageService = StorageFactories.getStorageService(record.getStorageType());
                if (storageService == null) {
                    continue;
                }
                storageService.delete(record.getFilePath());
                storageFileService.removeById(record.getId());
            }

            if (records.size() < pageSize) {
                // 没有更多数据
                break;
            }
            page++;
        }
    }

}
