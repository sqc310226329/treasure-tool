package com.treasure.mybatisPlus.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月07日
 * @see
 * @since 1.0
 */
@Getter
public enum GradeEnum {

    PRIMARY(1, "小学"),
    SECONDORY(2, "中学"),
    HIGH(3, "高中");

    GradeEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }


    private final int code;
    @EnumValue
    private final String descp;

}
