package vip.luosu.additional.storage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import vip.luosu.additional.common.BizException;
import vip.luosu.additional.storage.properties.StorageProperties;
import vip.luosu.additional.storage.service.StorageFactories;
import vip.luosu.additional.storage.service.StorageFileService;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class S3StorageService extends BaseStorageService implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private S3Client s3Client;

    public S3StorageService(StorageProperties storageProperties, StorageFileService storageFileService) {
        super(storageProperties, storageFileService);
    }

    public S3Client getClient() {
        return s3Client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                storageProperties.getS3().getAccessKey(),
                storageProperties.getS3().getSecretKey()
        );

        // 构建 S3Client 配置
        S3Configuration s3Configuration = S3Configuration.builder()
                // 设置 Path-Style Access
                .pathStyleAccessEnabled(storageProperties.getS3().isPathStyleAccess())
                .build();

        // 创建 S3Client
        this.s3Client = S3Client.builder()
                // 设置 Region
                .region(Region.of(storageProperties.getS3().getRegion()))
                // 设置 Endpoint
                .endpointOverride(URI.create(storageProperties.getS3().getEndpoint()))
                // 设置凭据
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                // 设置 S3 配置
                .serviceConfiguration(s3Configuration)
                .build();
        StorageFactories.registerStorageService(getType(), this);
    }

    @Override
    public String getType() {
        return "s3";
    }

    @Override
    public InputStream getInputStream(String source) {
        try {
            String bucketName = storageProperties.getS3().getBucketName();
            return getObject(bucketName, source);
        } catch (Exception e) {
            log.error("文件不存在", e);
            throw new BizException("文件不存在");
        }
    }

    @Override
    public void upload(InputStream inputStream, String source) {
        try {
            String bucketName = storageProperties.getS3().getBucketName();
            putObject(bucketName, source, inputStream);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BizException("上传文件失败");
        }
    }

    @Override
    public void copy(String source, String destination) {
        CopyObjectRequest request = CopyObjectRequest.builder()
                .sourceBucket(storageProperties.getS3().getBucketName())
                .sourceKey(source)
                .destinationBucket(storageProperties.getS3().getBucketName())
                .destinationKey(destination)
                .build();
        s3Client.copyObject(request);
    }

    @Override
    public void move(String source, String destination) {
        // 1. copy
        copy(source, destination);
        // 2. delete source
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(storageProperties.getS3().getBucketName())
                .key(source)
                .build();
        s3Client.deleteObject(deleteRequest);
    }

    @Override
    public void delete(String source) {
        try {
            String bucketName = storageProperties.getS3().getBucketName();
            removeObject(bucketName, source);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            throw new BizException("删除文件失败");
        }
    }

    @Override
    public boolean isExists(String source) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(storageProperties.getS3().getBucketName())
                    .key(source)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            s3Client.createBucket(b -> b.bucket(bucketName));
        }
    }

    /**
     * 获取全部bucket
     * <p>
     *
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
     * API Documentation</a>
     */
    public List<Bucket> getAllBuckets() {
        return s3Client.listBuckets().buckets();
    }

    /**
     * @param bucketName bucket名称
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
     * API Documentation</a>
     */
    public Optional<Bucket> getBucket(String bucketName) {
        return s3Client.listBuckets().buckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * @param bucketName bucket名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
     * Documentation</a>
     */
    public void removeBucket(String bucketName) {
        s3Client.deleteBucket(b -> b.bucket(bucketName));
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return S3ObjectSummary 列表
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
     * API Documentation</a>
     */
    public List<S3Object> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        return response.contents();
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        // 创建 S3Presigner 实例
        try (S3Presigner presigner = S3Presigner.create()) {
            // 构建请求
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();

            // 生成预签名 URL
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(p -> p.getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofDays(expires))); // 设置签名过期时间

            // 返回预签名 URL
            return presignedRequest.url().toString();
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public InputStream getObject(String bucketName, String objectName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        return s3Client.getObject(getObjectRequest);
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     */
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        return putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
     * API Documentation</a>
     */
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream, long size,
                                       String contextType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .contentType(contextType)
                .build();
        return s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(stream, size));

    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public HeadObjectResponse getObjectInfo(String bucketName, String objectName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        return s3Client.headObject(headObjectRequest);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS API
     * Documentation</a>
     */
    public void removeObject(String bucketName, String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
