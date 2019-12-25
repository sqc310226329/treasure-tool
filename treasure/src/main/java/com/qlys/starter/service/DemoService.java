package com.qlys.starter.service;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2019年12月23日
 * @see
 * @since 1.0
 */
public class DemoService {
    public String sayWhat;
    public String toWho;
    public DemoService(String sayWhat, String toWho){
        this.sayWhat = sayWhat;
        this.toWho = toWho;
    }
    public String say(){
        return this.sayWhat + "!  " + toWho;
    }
}
