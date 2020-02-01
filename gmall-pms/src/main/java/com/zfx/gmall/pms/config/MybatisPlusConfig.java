package com.zfx.gmall.pms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 分页查询 注意：使用mybatis-plus的分頁功能，必須配置其分頁插件
 * @author: zheng-fx
 * @time: 2020/1/18 0018 20:22
 */
@Configuration
public class MybatisPlusConfig {

    /*** 分页插件 */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
