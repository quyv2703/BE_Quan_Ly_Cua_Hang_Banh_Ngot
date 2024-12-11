package com.henrytran1803.BEBakeManage.common.exception;

import com.henrytran1803.BEBakeManage.common.exception.error.ProductError;

public class ProductException extends RuntimeException {
    private final ProductError error;

    public ProductException(ProductError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ProductError getError() {
        return error;
    }
}