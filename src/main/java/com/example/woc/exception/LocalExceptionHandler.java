package com.example.woc.exception;

import com.example.woc.enums.ErrorEnum;
import com.example.woc.response.GlobalResponse;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @Author xun
 * @create 2022/12/26 15:31
 */
@RestControllerAdvice
public class LocalExceptionHandler {
    @ExceptionHandler(LocalRuntimeException.class)
    public <T> GlobalResponse<T> localException(LocalRuntimeException e) {
        if (e == null) {
            return GlobalResponse.failure();
        }
        ErrorEnum errorEnum = e.getErrorEnum();
        if (errorEnum == null) {
            return GlobalResponse.failure(e.getMessage());
        }
        return GlobalResponse.failure(errorEnum);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> GlobalResponse<T> handlerValidationException(MethodArgumentNotValidException e) {
        //流处理，获取错误信息
        String messages = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return GlobalResponse.failure(messages);
    }
}
