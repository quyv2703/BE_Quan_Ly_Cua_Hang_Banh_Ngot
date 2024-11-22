package com.henrytran1803.BEBakeManage.common.exception.error;

public enum QuyExeption {
    SUCCESS("Q2000", "Operation successful"),
    AREA_NOT_FOUND("Q1001", "Area not found"),
    AREA_ALREADY_EXISTS("Q1004", "Area already exists"),
    TABLE_NOT_FOUND("Q1002", "Table not found"),
    PRODUCT_BATCH_NOT_FOUND("Q1003","Product batch not found"),
    BILL_NOT_FOUND("Q1005","Bill not found"),
    INVALID_BILL_STATUS("Q1006","Bill status invalid"),
    EMAIL_ALREADY_EXISTS("Q1007","Email already exists"),
    ROLE_NOT_FOUND("Q1008","Role not found"),
    USER_NOT_FOUND("Q1009","User not found");
    // Thuộc tính của Enum
    private final String code;
    private final String message;



    QuyExeption(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter cho mã lỗi
    public String getCode() {
        return code;
    }

    // Getter cho thông báo lỗi
    public String getMessage() {
        return message;
    }

    // Phương thức tiện lợi để lấy thông tin đầy đủ
    @Override
    public String toString() {
        return String.format("ErrorCode[code=%s, message=%s]", code, message);
    }
}
