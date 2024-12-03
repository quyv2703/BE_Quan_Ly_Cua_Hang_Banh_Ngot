package com.henrytran1803.BEBakeManage.common.exception.error;

public enum DailyDiscountError {
    INVALID_DISCOUNT_RANGE("INVALID_DISCOUNT_RANGE", "Giảm giá phải từ 1% đến 100%"),
    PRODUCT_BATCH_IDS_REQUIRED("PRODUCT_BATCH_IDS_REQUIRED", "Vui lòng chọn các lô hàng"),
    PRODUCT_BATCH_NOT_FOUND("PRODUCT_BATCH_NOT_FOUND", "Không tìm thấy lô hàng"),
    END_DATE_REQUIRED("END_DATE_REQUIRED", "Vui lòng chọn ngày kết thúc"),
    INVALID_END_DATE("INVALID_END_DATE", "Ngày kết thúc không hợp lệ"),
    SKIP_DEFAULT_REQUIRED("SKIP_DEFAULT_REQUIRED", "Vui lòng chọn có bỏ qua giảm giá mặc định"),
    CREATE_DISCOUNT_FAILED("CREATE_DISCOUNT_FAILED", "Tạo giảm giá thất bại"),
    CREATE_DISCOUNT_SUCCESS("CREATE_DISCOUNT_SUCCESS", "Tạo giảm giá thành công");

    private final String code;
    private final String message;

    DailyDiscountError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}