package vip.luosu.additional.common;

public class Result<T> {

    private String code;
    private String msg;
    private T data;
    private Boolean success;

    public String getCode() {
        return code;
    }

    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Result<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setData(data).setCode("0").setSuccess(true);
    }

    public static <T> Result<T> success() {
        return new Result<T>().setCode("0").setSuccess(true);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>().setCode("1099").setMsg(msg).setSuccess(false);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return new Result<T>().setCode(code).setMsg(msg).setSuccess(false);
    }
}
