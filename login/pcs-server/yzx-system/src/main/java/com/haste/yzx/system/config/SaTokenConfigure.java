package com.haste.yzx.system.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.haste.yzx.system.properties.WhiteListConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class SaTokenConfigure implements WebMvcConfigurer {

    @Resource
    private WhiteListConfig whiteListConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
     /*   List<String> urls = whiteListConfig.getUrls();
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(urls);
        WebMvcConfigurer.super.addInterceptors(registry);*/
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
