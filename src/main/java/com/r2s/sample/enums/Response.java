package com.r2s.sample.enums;

import lombok.Getter;

/**
 * Defining the enum
 *
 * @author kyle
 * @since 2023-09-01
 */
public class Response {

    //====== ADD 2023/09/01 kyle START ======//
    @Getter
    public static enum ResponseKey {
        STATUS("status"),
        DATA("data"),
        MESSAGE("message");

        private final String value;

        private ResponseKey(String value) {
            this.value = value;
        }

    }

    @Getter
    public static enum ResponseValue {
        FAILURE("0"),
        NOT_FOUND("0"),
        SUCCESSFULLY("1"),
        DUPLICATED("2");

        private final String value;

        private ResponseValue(String value) {
            this.value = value;
        }
    }
    //====== ADD 2023/09/01 kyle END ======//
}