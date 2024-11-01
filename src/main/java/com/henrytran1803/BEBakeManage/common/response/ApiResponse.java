package com.henrytran1803.BEBakeManage.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private  String errorcode;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null,null, data);
    }

    public static <T> ApiResponse<T> error(String errorcode,String message) {
        return new ApiResponse<>(false, message, errorcode,null);
    }
}