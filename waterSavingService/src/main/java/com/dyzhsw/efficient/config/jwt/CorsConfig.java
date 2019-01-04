package com.dyzhsw.efficient.config.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhaoqingjie
 * CORS过滤器
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

	/**
	 * 1、过滤器阶段的CORS：CorsFilter
	 */
	@Bean
	public FilterRegistrationBean corsFilterRegistrationBean() {
		// 对响应头进行CORS授权
		DefaultCorsRegistration corsRegistration = new DefaultCorsRegistration("/**");
		this._configCorsParams(corsRegistration);
		// 注册CORS过滤器
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", corsRegistration.getCorsConfiguration());
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(configurationSource));
		bean.setOrder(0);
		return bean;
	}

	private void _configCorsParams(CorsRegistration corsRegistration) {
		corsRegistration.allowedOrigins("*")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.HEAD.name())
				.allowedHeaders("*")
				.exposedHeaders(HttpHeaders.SET_COOKIE)
				.allowCredentials(true)
				.maxAge(1800);
	}
}

