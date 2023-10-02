package com.r2s.mobilestore.enums;

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
    public static enum Key {
        STATUS;
    }

    @Getter
    public static enum Value {
        FAILURE,
        SUCCESSFULLY,
        DUPLICATED,
        NOT_FOUND;
    }
    //====== ADD 2023/09/01 kyle END ======//
}