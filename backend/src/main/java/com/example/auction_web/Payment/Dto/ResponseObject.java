package com.example.auction_web.Payment.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nimbusds.jose.Payload;
import lombok.Builder;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseObject <T> extends ResponseEntity<ResponseObject.Payload<T>> {
    public ResponseObject(HttpStatusCode code, String message, T data) {
        super(new Payload<>(code.value(), message, data),code);
    }

    @Builder
    public static class Payload<T> {
        public int code;
        public String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public T data;
    }

}
