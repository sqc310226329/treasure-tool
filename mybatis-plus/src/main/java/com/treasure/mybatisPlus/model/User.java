package com.treasure.mybatisPlus.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月06日
 * @see
 * @since 1.0
 */
@Data
public class User {
    @TableField()
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
