package com.r2s.sample.dtos;

import com.r2s.sample.enums.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

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
