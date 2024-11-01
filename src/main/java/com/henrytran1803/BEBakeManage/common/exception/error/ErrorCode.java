package com.henrytran1803.BEBakeManage.common.exception.error;

public enum ErrorCode {
    USER_NOT_FOUND("1001", "User not found"),
    INVALID_CREDENTIALS("1002", "Invalid credentials"),
    PRODUCT_NOT_FOUND("1003", "Product not found"),
    ORDER_NOT_FOUND("1004", "Order not found"),
    IMAGE_UPLOAD_FAILED("1005", "Image upload failed"),
    IMAGE_DELETE_FAILED("1006", "Image delete failed"),
    CATEGORY_NOT_FOUND("1007", "Category not found"),
    CATEGORY_CREATE_FAIL("1015", "Category create fail"),

    CATEGORY_DELETE_FAILED("1008", "Category couldn't be deleted"),
    CATEGORY_UPDATE_FAILED("1009", "Category couldn't be updated"),
    RECIPE_NOT_FOUND("1010", "Recipe not found"),
    RECIPE_CREATE_FAILED("1011", "Recipe creation failed"),
    RECIPE_UPDATE_FAILED("1012", "Recipe update failed"),
    RECIPE_DELETE_FAILED("1013", "Recipe deletion failed"),
    RECIPE_DETAIL_NOT_FOUND("1014", "Recipe detail not found"),
    INTERNAL_SERVER_ERROR("1016", "INTERNAL_SERVER_ERROR");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
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
