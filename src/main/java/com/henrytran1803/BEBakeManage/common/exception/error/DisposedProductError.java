package com.henrytran1803.BEBakeManage.common.exception.error;

public enum DisposedProductError {
    DISPOSED_PRODUCT_NOT_FOUND("DISPOSED_PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm cần hủy"),
    BATCH_IDS_REQUIRED("BATCH_IDS_REQUIRED", "Vui lòng chọn các lô hàng cần hủy"),
    BATCH_NOT_FOUND("BATCH_NOT_FOUND", "Không tìm thấy lô hàng"),
    DISPOSED_FAILED("DISPOSED_FAILED", "Hủy sản phẩm thất bại"),
    DISPOSED_SUCCESS("DISPOSED_SUCCESS", "Hủy sản phẩm thành công"),
    INVALID_BATCH_STATUS("INVALID_BATCH_STATUS", "Trạng thái lô hàng không hợp lệ để hủy");

    private final String code;
    private final String message;

    DisposedProductError(String code, String message) {
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