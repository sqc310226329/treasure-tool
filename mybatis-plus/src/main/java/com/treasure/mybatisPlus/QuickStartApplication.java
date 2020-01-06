package com.treasure.mybatisPlus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月06日
 * @see
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.treasure.mybatisPlus.mapper")
public class QuickStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);
    }

}
