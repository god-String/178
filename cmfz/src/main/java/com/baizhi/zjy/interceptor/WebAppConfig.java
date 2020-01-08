package com.baizhi.zjy.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    //实现拦截器 要拦截的路径以及不拦截的路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        /**
        * addPathPatterns//这个是添加拦截路径，excludePathPatterns这个是排除拦截的路径多个路径中间用逗号隔开，
        * */
     /*   registry.addInterceptor(new MyInterceptor1())
                .addPathPatterns("/**")
                .excludePathPatterns("/boot/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/jqgrid/**")
                .excludePathPatterns("/json/**")
                .excludePathPatterns("/login.jsp")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/chu/**")
                .excludePathPatterns("/upload/**");*/
    }
}
