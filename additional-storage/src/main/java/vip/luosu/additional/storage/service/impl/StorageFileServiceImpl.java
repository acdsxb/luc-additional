package vip.luosu.additional.storage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.luosu.additional.storage.entity.StorageFile;
import vip.luosu.additional.storage.mapper.StorageFileMapper;
import vip.luosu.additional.storage.service.StorageFileService;

/**
 * @author enuao
 * @description 针对表【attachment_info(附件信息)】的数据库操作Service实现
 * @createDate 2025-11-29 15:13:47
 */
public class StorageFileServiceImpl extends ServiceImpl<StorageFileMapper, StorageFile>
        implements StorageFileService {

    @Override
    public StorageFile getByFileId(String fileId) {
        return getOne(Wrappers.<StorageFile>lambdaQuery().eq(StorageFile::getFileId, fileId));
    }
}
