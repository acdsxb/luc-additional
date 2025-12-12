package vip.luosu.additional.storage.entity;

public class UploadInfo {

    /**
     * 绑定数据id
     */
    private String bindId;

    /**
     * 绑定数据类型
     */
    private String bindType;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    public String getBindId() {
        return bindId;
    }

    public UploadInfo setBindId(String bindId) {
        this.bindId = bindId;
        return this;
    }

    public String getBindType() {
        return bindType;
    }

    public UploadInfo setBindType(String bindType) {
        this.bindType = bindType;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public UploadInfo setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public UploadInfo setOperatorName(String operatorName) {
        this.operatorName = operatorName;
        return this;
    }
}
