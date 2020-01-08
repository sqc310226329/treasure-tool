package com.treasure.mybatisPlus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.treasure.mybatisPlus.handler.MyMetaObjectHandler;
import com.treasure.mybatisPlus.yml.YmlPropertyLoaderFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月07日
 * @see
 * @since 1.0
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.treasure.**.mapper.**")
@PropertySource(
        value = "classpath:/treasure-mybatis.yml",
        factory = YmlPropertyLoaderFactory.class,
        encoding="UTF-8"
)
public class MybatisPlusConfig {

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler myMetaObjectHandler(){
        return new MyMetaObjectHandler();
    }
}
