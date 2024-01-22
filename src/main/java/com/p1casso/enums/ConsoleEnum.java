package com.p1casso.enums;

import lombok.Getter;

/**
 * 主机类型枚举类
 */
@Getter
public enum ConsoleEnum {

    /**
     * PS5
     */
    PS5(0),

    /**
     * PS4和其他
     */
    PS4_AND_OTHERS(1);

    private final Integer url;

    ConsoleEnum(Integer url) {
        this.url = url;
    }
}