package com.henrytran1803.BEBakeManage.common.exception.error;

public enum ProductError {
    PRODUCT_NAME_EXISTS("PRODUCT_NAME_EXISTS","Tên sản phẩm đã tồn tại"),
    RECIPE_NOT_EXISTS("RECIPE_NOT_EXISTS","Không tồn tại công thức"),
    CATEGORY_NOT_EXISTS("CATEGORY_NOT_EXISTS", "Không tồn tại danh mục"),
    CONNECT_ERROR("CONNECT_ERROR", "Có lỗi xảy ra khi lưu sản phẩm"),
    POST_SUCCESS("POST_SUCCESS", "Lưu sản phẩm thành công");

    private final String code;
    private final String message;

    ProductError(String code, String message) {
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