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
    INTERNAL_SERVER_ERROR("1016", "INTERNAL_SERVER_ERROR"),
    PRODUCT_CREATION_FAILED("P002", "Failed to create product"),
    PRODUCT_UPDATE_FAILED("P003", "Failed to update product"),
    PRODUCT_DELETION_FAILED("P004", "Failed to delete product"),
    INVALID_PRODUCT_DATA("P005", "Invalid product data"),
    PRICE_UPDATE_FAILED("P006", "Failed to update product price"),
    INVALID_PRICE("P007", "Invalid price value"),
    INVALID_CATEGORY("P008", "Invalid category"),
    INVALID_RECIPE("P009", "Invalid recipe"),
    INVALID_DIMENSIONS("P010", "Invalid product dimensions"),
    DUPLICATE_PRODUCT_NAME("P011","Duplicate product" ),
    PROMOTION_NOT_FOUND("PR001", "Promotion not found"),
    PROMOTION_CREATE_FAILED("PR002", "Failed to create promotion"),
    PROMOTION_UPDATE_FAILED("PR003", "Failed to update promotion"),
    PROMOTION_DELETE_FAILED("PR004", "Failed to delete promotion"),
    INVALID_PROMOTION_DATA("PR005", "Invalid promotion data"),
    PROMOTION_STATUS_UPDATE_FAILED("PR006", "Failed to update promotion status"),
    PROMOTION_PRODUCT_NOT_FOUND("PR007", "Product not found in promotion"),
    PROMOTION_DETAIL_DELETE_FAILED("PR008", "Failed to delete promotion detail"),
    PROMOTION_DATE_INVALID("PR009", "Invalid promotion dates"),
    PROMOTION_DISCOUNT_INVALID("PR010", "Invalid discount value"),
    PROMOTION_PRODUCT_ALREADY_EXISTS("PR011", "Product already exists in promotion"),
    PROMOTION_EXPIRED("PR012", "Promotion has expired"),
    PROMOTION_NOT_ACTIVE("PR013", "Promotion is not active");

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
