package com.hope.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 访问映射配置
 *
 * @author AoDeng
 * @date 16:21 21-8-2
 */
@Configuration
public class HopeMvcConfig implements WebMvcConfigurer {


    /**
     * 将自定义拦截器放入spring容器中
     */
    @Bean
    public TokenCheckFilter getTokenCheckFilter(){
        return new TokenCheckFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //所以请求都走token验证拦截器
        registry.addInterceptor(getTokenCheckFilter()).addPathPatterns("/**");
    }
}
