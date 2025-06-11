package io.gmenezes.infuse_api.adapters.dtos;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse<T> {
    private int status;
    private String message;
    private T content;
    private List<AppErrorResponse> errors;

    public static <T> AppResponse<T> ok(String message, T content) {
        return new AppResponse<>(HttpStatus.OK.value(), message, content, null);
    }

    public static <T> AppResponse<T> invalid(String message, HttpStatus httpStatus, List<AppErrorResponse> errors) {
        return new AppResponse<>(httpStatus.value(), message, null, errors);
    }

    public static <T> AppResponse<T> fromException(String message, HttpStatus httpStatus, String traceId, Exception ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .description(ex.getMessage())
                .traceId(traceId)
                .build();
        return invalid(message, httpStatus, Collections.singletonList(error));
    }

    public static <T> AppResponse<T> error(String message, HttpStatus httpStatus, String description, String traceId) {
        AppErrorResponse error = AppErrorResponse.builder()
                .description(description)
                .traceId(traceId)
                .build();
        return invalid(message, httpStatus, Collections.singletonList(error));
    }

    public static <T> AppResponse<T> noContent(String message) {
        return new AppResponse<>(HttpStatus.NO_CONTENT.value(), message, null, null);
    }

    public ResponseEntity<AppResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}