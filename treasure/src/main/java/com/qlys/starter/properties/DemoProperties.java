package com.qlys.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2019年12月23日
 * @see
 * @since 1.0
 */
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private String sayWhat;
    private String toWho;

    public String getSayWhat() {
        return sayWhat;
    }

    public void setSayWhat(String sayWhat) {
        this.sayWhat = sayWhat;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }
}