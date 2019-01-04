package com.dyzhsw.efficient.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhaoqingjie
 */
@Configuration
@EnableConfigurationProperties({JwtConfigBean.class})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	JwtConfigBean jwtConfigBean;

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

	/**
     * 拦截器
     * @param registry
	 * addPathPatterns 用于添加拦截规则
	 * excludePathPatterns 用户排除拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
        registry.addInterceptor(new TokenInterceptor())
        .addPathPatterns(jwtConfigBean.getPathPatterns())
        .excludePathPatterns(jwtConfigBean.getExcludePathPatterns());
    }

}
