package com.example.woc.config;

import com.example.woc.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author xun
 * @create 2023/1/3 14:34
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private UserInterceptor userInterceptor;

    @Autowired
    public void setUserInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/user/register");
    }
}

