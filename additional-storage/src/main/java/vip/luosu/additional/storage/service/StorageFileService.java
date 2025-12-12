package vip.luosu.additional.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.luosu.additional.storage.entity.StorageFile;

public interface StorageFileService extends IService<StorageFile> {

    StorageFile getByFileId(String fileId);
}
