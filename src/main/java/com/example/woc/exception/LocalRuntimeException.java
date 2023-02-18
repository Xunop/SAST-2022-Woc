package com.example.woc.exception;

import com.example.woc.enums.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author xun
 * @create 2022/12/26 15:31
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class LocalRuntimeException extends RuntimeException {

    private ErrorEnum errorEnum;

    public LocalRuntimeException(String message) {
        super(message);
    }

    public LocalRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errorEnum = errorEnum;
    }
}
