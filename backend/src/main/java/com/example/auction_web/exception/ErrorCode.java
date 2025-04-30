package com.example.auction_web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1009, "Category not existed", HttpStatus.NOT_FOUND),
    BALANCE_USER_NOT_EXISTED(1010, "Balance user not existed", HttpStatus.NOT_FOUND),
    REGISTER_SESSION_EXISTED(1011, "Register session existed", HttpStatus.BAD_REQUEST),
    TYPE_NOT_EXISTED(1014, "Type not existed", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_EXISTED(1015, "Address not existed", HttpStatus.NOT_FOUND),
    ASSET_STATUS_NOT_FOUND(1016, "Asset status not existed", HttpStatus.NOT_FOUND),
    INSRECTOR_NOT_EXISTED(1017, "Insprector not existed", HttpStatus.NOT_FOUND),
    ASSET_NOT_EXISTED(1018, "Asset not existed", HttpStatus.NOT_FOUND),
    AUCTION_SESSION_NOT_EXISTED(1019, "Auction session not existed", HttpStatus.NOT_FOUND),
    AUCTION_HISTORY_NOT_EXISTED(1021, "Auction history not existed", HttpStatus.NOT_FOUND),
    ACCOUNT_UNVERIFIED(1022, "Unconfirmed user", HttpStatus.NOT_FOUND),
    DEPOSIT_NOT_EXISTED(1023, "Deposit not existed", HttpStatus.NOT_FOUND),
    BILL_NOT_EXISTED(1024, "Bill not existed", HttpStatus.NOT_FOUND),
    IMAGE_ASSET_NOT_EXISTED(1025, "Image asset not existed", HttpStatus.NOT_FOUND),
    REGISTER_SESSION_NOT_EXISTED(1026, "Register session not existed", HttpStatus.NOT_FOUND),
    REGISTER_AUCTION_NOT_EXISTED(1027, "Register auction not existed", HttpStatus.NOT_FOUND),
    INSPECTOR_NOT_EXISTED(1028, "Inspector not existed", HttpStatus.NOT_FOUND),
    REQUIREMENT_NOT_EXISTED(1029, "Requirement not existed", HttpStatus.NOT_FOUND),
    IMAGE_REQUIREMENT_NOT_EXISTED(1030, "Image requirement not existed", HttpStatus.NOT_FOUND),
    EMAIL_VERIFICATION_TOKEN_NOT_EXIST(1031, "Email verification token not existed", HttpStatus.NOT_FOUND),
    EVENT_NOT_EXIST(1032, "Event not existed", HttpStatus.NOT_FOUND),
    FAVOURITE_NOT_EXISTED(1033, "Favourite not existed", HttpStatus.NOT_FOUND),
    DEPOSIT_PRICE_IS_GREATER_THAN_MAX_DEPOSIT(1034, "Deposit price is greater than max deposit", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1035, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_CREATE_INVALID(1036, "User create invalid", HttpStatus.BAD_REQUEST),
    BID_PRICE_MUST_GREATER_THAN_MAX_BID_PRICE(1037, "Bid price must be greater than max bid price", HttpStatus.BAD_REQUEST),
    CONCURRENT_UPDATE(1038, "Concurrent update", HttpStatus.BAD_REQUEST),
    CREATE_AUTO_BID_FAILED(1039, "Create auto bid failed", HttpStatus.BAD_REQUEST),
    USER_CANNOT_BID_HIGHER_THAN_HIS_BID(1040, "You cannot bid higher than your bid", HttpStatus.BAD_REQUEST),
    AUTOBID_NOT_EXISTED(1041, "Auto bid not existed", HttpStatus.NOT_FOUND),
    UPDATE_AUTO_BID_FAILED(1042, "Update auto bid failed", HttpStatus.BAD_REQUEST),
    BALANCE_NOT_ENOUGH(1043, "Balance not enough", HttpStatus.BAD_REQUEST),
    CREATE_NOTIFICATION_FAILED(1044, "Create notification failed", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_EXISTED(1045, "Notification not existed", HttpStatus.NOT_FOUND),
    CREATE_CONVERSATION_FAILED(1046, "Create conversation failed", HttpStatus.BAD_REQUEST),
    CREATE_REGISTER_SESSION_FAILED(1047, "Create register session failed", HttpStatus.BAD_REQUEST),
    CREATE_BOT_CONVERSATION_FAILED(1048, "Create bot conversation failed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
