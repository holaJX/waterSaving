package com.dyzhsw.efficient.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: pjx
 * @Date: 2018/12/7 11:51
 * @Version 1.0
 */
public class ContextUser {
    public ContextUser() {
    }
    public static Claims getClaims() {

        ServletRequestAttributes newattribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = newattribute.getRequest();
        Object object = request.getAttribute("claims");
        if (object != null) {
            return (Claims) object;
        }
        return null;
    }
    public static String getUserId() {
        ServletRequestAttributes newattribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = newattribute.getRequest();
        Object object = request.getAttribute("claims");
        if (object != null) {
            Claims  claims= (Claims) object;
            System.out.println("当前登录app的用户信息为："+claims.get("loginName").toString() +"--->" +claims.get("id"));
           return claims.get("id").toString();
        }
        return "";
    }

}
