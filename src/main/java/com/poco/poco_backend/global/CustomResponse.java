package com.poco.poco_backend.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private final T result;

    public static <T> CustomResponse<T> onSuccess(T result) {
        return new CustomResponse<>(true, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), result);
    }

    public static <T> CustomResponse<T> onSuccess(HttpStatus status, T result) {
        return new CustomResponse<>(true, String.valueOf(status.value()), status.getReasonPhrase(), result);
    }

    public static <T> CustomResponse<T> onFailure(String code, String message, T result) {
        return new CustomResponse<>(false, code, message, result);
    }

    public static <T> CustomResponse<T> onFailure(String code, String message) {
        return new CustomResponse<>(false, code, message, null);
    }

    public static <T> CustomResponse<T> onFailure(HttpStatus httpStatus, String code, String message) {
        return new CustomResponse<>(false, code, message, null);
    }

    public static <T> CustomResponse<T> onFailure(HttpStatus status, T result) {
        return new CustomResponse<>(false, String.valueOf(status.value()), status.getReasonPhrase(), result);
    }

}