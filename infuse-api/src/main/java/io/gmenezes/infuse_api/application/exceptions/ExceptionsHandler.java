package io.gmenezes.infuse_api.application.exceptions;

import io.gmenezes.infuse_api.adapters.dtos.AppErrorResponse;
import io.gmenezes.infuse_api.adapters.dtos.AppResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<AppErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> AppErrorResponse.builder()
                        .code("VALIDATION_ERROR")
                        .description(error.getDefaultMessage())
                        .traceId(request.getHeader("X-Trace-ID"))
                        .build())
                .toList();

        return AppResponse.invalid("Erro de validação", HttpStatus.BAD_REQUEST, errors)
                .toResponseEntity();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppResponse<Object>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<AppErrorResponse> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> AppErrorResponse.builder()
                        .code("CONSTRAINT_VIOLATION")
                        .description(violation.getMessage())
                        .traceId(request.getHeader("X-Trace-ID"))
                        .build())
                .toList();

        return AppResponse.invalid("Erro de validação de restrições", HttpStatus.BAD_REQUEST, errors)
                .toResponseEntity();
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handleObjectNotFound(
            ObjectNotFoundException ex, WebRequest request) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("RESOURCE_NOT_FOUND")
                .description(ex.getMessage())
                .traceId(request.getHeader("X-Trace-ID"))
                .build();

        return AppResponse.invalid("Recurso não encontrado", HttpStatus.NOT_FOUND,
                Collections.singletonList(error)).toResponseEntity();
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<AppResponse<Object>> handleKafkaExceptions(
            KafkaException ex, WebRequest request) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("KAFKA_ERROR")
                .description("Erro ao processar mensagem: " + ex.getMessage())
                .traceId(request.getHeader("X-Trace-ID"))
                .build();

        return AppResponse.invalid("Erro no serviço de mensageria", HttpStatus.SERVICE_UNAVAILABLE,
                Collections.singletonList(error)).toResponseEntity();
    }

    @ExceptionHandler(org.apache.kafka.common.errors.TimeoutException.class)
    public ResponseEntity<AppResponse<Object>> handleKafkaTimeoutExceptions(
            org.apache.kafka.common.errors.TimeoutException ex, WebRequest request) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("KAFKA_TIMEOUT")
                .description("Timeout ao se comunicar com o serviço de mensageria: " + ex.getMessage())
                .traceId(request.getHeader("X-Trace-ID"))
                .build();

        return AppResponse.invalid("Timeout no serviço de mensageria", HttpStatus.GATEWAY_TIMEOUT,
                Collections.singletonList(error)).toResponseEntity();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AppResponse<Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("DATA_INTEGRITY_ERROR")
                .description("Violação de integridade de dados: " + ex.getMostSpecificCause().getMessage())
                .traceId(request.getHeader("X-Trace-ID"))
                .build();

        return AppResponse.invalid("Erro de integridade de dados", HttpStatus.CONFLICT,
                Collections.singletonList(error)).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleGenericExceptions(
            Exception ex, WebRequest request) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .description("Erro interno no servidor: " + ex.getMessage())
                .traceId(request.getHeader("X-Trace-ID"))
                .build();

        log.error("Erro não tratado: ", ex);

        return AppResponse.invalid("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(error)).toResponseEntity();
    }
}