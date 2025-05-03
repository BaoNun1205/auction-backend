package com.example.auction_web.exception;

public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private String customMessage;

    // Constructor dùng ErrorCode và message mặc định
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = errorCode.getMessage();
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
