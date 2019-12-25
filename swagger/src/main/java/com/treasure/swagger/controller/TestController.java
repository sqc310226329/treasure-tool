package com.treasure.swagger.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.treasure.swagger.model.ReqEntity;
import com.treasure.swagger.model.Rest;
import com.treasure.swagger.model.WorkExperience;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2019年12月25日
 * @see
 * @since 1.0
 */

@Api(value = "测试接口",tags = "测试接口")
@RestController
@RequestMapping("/test/api")
public class TestController {

    /**
     * 管理端-货源列表
     */
    @GetMapping
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "管理端货源列表",notes = "管理端货源列表-分页-条件查询")
    public Rest<ReqEntity> obj(String name){

        System.out.println(name);
        ReqEntity reqEntity=new ReqEntity();
        reqEntity.getWorkExperiences().add(new WorkExperience("阿里妈妈","董事长","2011","2018"));
        Rest<ReqEntity> reqEntityRest=new Rest<>();
        reqEntityRest.setData(reqEntity);
        return reqEntityRest;
    }
}
