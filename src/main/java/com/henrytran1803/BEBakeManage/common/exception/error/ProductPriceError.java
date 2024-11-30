package com.henrytran1803.BEBakeManage.common.exception.error;

public enum ProductPriceError {
    PRICE_NOT_FOUND("PRICE_NOT_FOUND", "Không tìm thấy thông tin giá sản phẩm"),
    PRICE_INPUT_REQUIRED("PRICE_INPUT_REQUIRED", "Vui lòng nhập giá sản phẩm"),
    PRICE_INVALID_FORMAT("PRICE_INVALID_FORMAT", "Định dạng giá không hợp lệ"),
    PRICE_INVALID_RANGE("PRICE_INVALID_RANGE", "Giá phải lớn hơn 1,000đ"),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm"),
    UPDATE_PRICE_FAILED("UPDATE_PRICE_FAILED", "Cập nhật giá thất bại"),
    GET_HISTORY_FAILED("GET_HISTORY_FAILED", "Lấy lịch sử giá thất bại");

    private final String code;
    private final String message;

    ProductPriceError(String code, String message) {
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