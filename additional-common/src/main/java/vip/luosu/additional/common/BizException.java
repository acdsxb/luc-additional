package vip.luosu.additional.common;

import java.io.Serial;

public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5762392452346026766L;

    private String code = "1099";

    public String getCode() {
        return code;
    }

    public BizException setCode(String code) {
        this.code = code;
        return this;
    }

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
