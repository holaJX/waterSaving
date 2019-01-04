package com.dyzhsw.efficient.config.jwt;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

/**
 * @author zhaoqingjie
 * CORS过滤器
 */
public class DefaultCorsRegistration extends CorsRegistration {

    public DefaultCorsRegistration(String pathPattern) {
        super(pathPattern);
    }

    @Override
    protected CorsConfiguration getCorsConfiguration() {
        return super.getCorsConfiguration();
    }
}
