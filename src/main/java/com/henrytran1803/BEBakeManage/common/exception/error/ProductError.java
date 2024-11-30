package com.henrytran1803.BEBakeManage.common.exception.error;

public enum ProductError {
    PRODUCT_NAME_INPUT_ERROR("PRODUCT_NAME_INPUT_ERROR", "Vui lòng nhập tên sản phẩm"),
    PRODUCT_DESC_INPUT_ERROR("PRODUCT_DESC_INPUT_ERROR", "Vui lòng nhập mô tả sản phẩm"),
    PRODUCT_PRICE_INPUT_ERROR("PRODUCT_PRICE_INPUT_ERROR", "Vui lòng nhập giá sản phẩm"),
    PRODUCT_PRICE_INPUT_ERROR2("PRODUCT_PRICE_INPUT_ERROR2", "Giá sản phẩm không được nhỏ hơn 1,000đ"),
    PRODUCT_EXPIRY_INPUT_ERROR1("PRODUCT_EXPIRY_INPUT_ERROR1", "Vui lòng nhập thời hạn sử dụng"),
    PRODUCT_EXPIRY_INPUT_ERROR2("PRODUCT_EXPIRY_INPUT_ERROR2", "Định dạng thời hạn sử dụng không hợp lệ"),
    PRODUCT_LENGTH_INPUT_ERROR1("PRODUCT_LENGTH_INPUT_ERROR1", "Vui lòng nhập chiều dài sản phẩm"),
    PRODUCT_LENGTH_INPUT_ERROR2("PRODUCT_LENGTH_INPUT_ERROR2", "Chiều dài sản phẩm phải từ 1cm đến 200cm"),
    PRODUCT_HEIGHT_INPUT_ERROR1("PRODUCT_HEIGHT_INPUT_ERROR1", "Vui lòng nhập chiều cao sản phẩm"),
    PRODUCT_HEIGHT_INPUT_ERROR2("PRODUCT_HEIGHT_INPUT_ERROR2", "Chiều cao sản phẩm phải từ 1cm đến 200cm"),
    PRODUCT_WIDTH_INPUT_ERROR1("PRODUCT_WIDTH_INPUT_ERROR1", "Vui lòng nhập chiều rộng sản phẩm"),
    PRODUCT_WIDTH_INPUT_ERROR2("PRODUCT_WIDTH_INPUT_ERROR2", "Chiều rộng sản phẩm phải từ 1cm đến 200cm"),
    PRODUCT_WEIGHT_INPUT_ERROR1("PRODUCT_WEIGHT_INPUT_ERROR1", "Vui lòng nhập khối lượng sản phẩm"),
    PRODUCT_WEIGHT_INPUT_ERROR2("PRODUCT_WEIGHT_INPUT_ERROR2", "Khối lượng sản phẩm phải từ 1g đến 20,000g"),
    CATEGORY_REQUIRED_ERROR("CATEGORY_REQUIRED_ERROR", "Vui lòng chọn danh mục"),
    RECIPE_REQUIRED_ERROR("RECIPE_REQUIRED_ERROR", "Vui lòng chọn công thức"),
    PRODUCT_NAME_LENGTH_ERROR("PRODUCT_NAME_LENGTH_ERROR", "Tên sản phẩm không được vượt quá 250 ký tự"),
    PRODUCT_DESC_LENGTH_ERROR("PRODUCT_DESC_LENGTH_ERROR", "Mô tả không được vượt quá 250 ký tự"),
    PRODUCT_EXPIRY_WARNING_ERROR("PRODUCT_EXPIRY_WARNING_ERROR", "Cảnh báo hạn sử dụng phải lớn hơn 0"),
    PRODUCT_DISCOUNT_LIMIT_ERROR("PRODUCT_DISCOUNT_LIMIT_ERROR", "Giới hạn giảm giá phải từ 0 đến 100"),
    PRODUCT_IMAGE_REQUIRED_ERROR("PRODUCT_IMAGE_REQUIRED_ERROR", "Vui lòng chọn ít nhất một ảnh"),
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