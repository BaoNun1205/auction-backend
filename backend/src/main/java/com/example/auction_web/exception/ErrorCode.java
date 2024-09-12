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
    COIN_USER_NOT_EXISTED(1010, "Coin user not existed", HttpStatus.NOT_FOUND),
    PROVINCE_NOT_EXISTED(1011, "Province not existed", HttpStatus.NOT_FOUND),
    DISTRICT_NOT_EXISTED(1012, "District not existed", HttpStatus.NOT_FOUND),
    WARD_NOT_EXISTED(1013, "Ward not existed", HttpStatus.NOT_FOUND),
    TYPE_NOT_EXISTED(1014, "Type not existed", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_EXISTED(1015, "Address not existed", HttpStatus.NOT_FOUND),
    ASSET_STATUS_NOT_FOUND(1016, "Asset status not existed", HttpStatus.NOT_FOUND),
    INSRECTOR_NOT_EXISTED(1017, "Insprector not existed", HttpStatus.NOT_FOUND),
    ASSET_NOT_EXISTED(1018, "Asset not existed", HttpStatus.NOT_FOUND),
    AUCTION_SESSION_NOT_EXISTED(1019, "Auction session not existed", HttpStatus.NOT_FOUND),
    AUCTION_ITEM_NOT_EXISTED(1020, "Auction item not existed", HttpStatus.NOT_FOUND),
    AUCTION_HISTORY_NOT_EXISTED(1021, "Auction history not existed", HttpStatus.NOT_FOUND),
    AUCTION_HISTORY_DETAIL_NOT_EXISTED(1022, "Auction history detail not existed", HttpStatus.NOT_FOUND),
    DEPOSIT_NOT_EXISTED(1023, "Deposit not existed", HttpStatus.NOT_FOUND),
    BILL_NOT_EXISTED(1024, "Bill not existed", HttpStatus.NOT_FOUND),
    IMAGE_ASSET_NOT_EXISTED(1025, "Image asset not existed", HttpStatus.NOT_FOUND),
    BILL_ITEM_NOT_EXISTED(1026, "Bill item not existed", HttpStatus.NOT_FOUND),
    REGISTER_AUCTION_NOT_EXISTED(1027, "Register auction not existed", HttpStatus.NOT_FOUND),
    INSPECTOR_NOT_EXISTED(1028, "Inspector not existed", HttpStatus.NOT_FOUND),
    REQUIREMENT_NOT_EXISTED(1029, "Requirement not existed", HttpStatus.NOT_FOUND),
    IMAGE_REQUIREMENT_NOT_EXISTED(1030, "Image requirement not existed", HttpStatus.NOT_FOUND),
    EMAIL_VERIFICATION_TOKEN_NOT_EXIST(1031, "Email verification token not existed", HttpStatus.NOT_FOUND),
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
