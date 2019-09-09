package tartan.report.core;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ResponseResult<T> {
    private boolean success;
    private String msg;
    private @Nullable T data;

    public @UnknownInitialization ResponseResult() {
    }

    public ResponseResult(boolean success, String msg, @Nullable T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public @Nullable T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }
}
