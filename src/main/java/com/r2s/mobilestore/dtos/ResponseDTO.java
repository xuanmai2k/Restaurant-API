package com.r2s.mobilestore.dtos;

import com.r2s.mobilestore.enums.Response;

import java.util.LinkedHashMap;

/**
 * Custom response status
 *
 * @author kyle
 * @since 2023-09-01
 */
public class ResponseDTO extends LinkedHashMap<Response.Key, Object> {
    private static ResponseDTO instance;

    public static synchronized ResponseDTO getInstance() {
        if (instance == null) {
            instance = new ResponseDTO();
        }

        return instance;
    }

    public void setResponse(Response.Key key, Object value) {
        this.clear();
        this.put(key, value);
    }
}
