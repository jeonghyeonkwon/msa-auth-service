package com.jeonghyeon.authservice.exhandler.advice;

import com.jeonghyeon.authservice.dto.ResponseDto;
import com.jeonghyeon.authservice.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseDto illegalExHandler(IllegalStateException e){
        log.error("[exceptionHandler] e : " + e);
        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
