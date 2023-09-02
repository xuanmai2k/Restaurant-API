package com.r2s.sample.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom response status
 *
 * @author kyle
 * @since 2023-09-01
 */
@Setter
@Getter
public class ResponseDto<T> {

    /** Represents the response code. */
    private T responseCode;

    /** Represents the response description. */
    private String responseDescription;

    /**
     * Constructor for class ResponseDto
     *
     * @param code Response code
     * @param description Response description
     */
    public ResponseDto(T code, String description) {
        this.responseCode = code;
        this.responseDescription = description;
    }
}
