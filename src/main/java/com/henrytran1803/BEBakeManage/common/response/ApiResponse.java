package com.henrytran1803.BEBakeManage.common.response;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
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
    public static <T> ApiResponse<T> Q_success(T data, QuyExeption quyExeption) {
        return new ApiResponse<>(true, quyExeption.getCode(), quyExeption.getMessage(), data);
    }
    public static <T> ApiResponse<T> Q_failure(T data, QuyExeption quyExeption) {
        return new ApiResponse<>(false, quyExeption.getCode(), quyExeption.getMessage(), data);
    }
    public static <T> ApiResponse<T> Q_failure(T data, QuyExeption error, String details) {
        return new ApiResponse<>(false, error.getMessage() + ": " + details, error.getCode(), data);
    }

}