package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.constant.ManureUrl;
import com.dyzhsw.efficient.dto.ResultObjectDTO;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TerminalControllerTest extends BaseJunit {
    @Value("${jxwater.manureservice}")
    private String manureHost;
    /**
     * 阀控器接口地址
     */
    @Value("${jxwater.microcontrol}")
    private String microcontrolHost;

    @Test
    public void testIsOnline() throws IOException {
        String terminalId = "223344";//终端编号
        HashMap<String, String> map = new HashMap<>();
        map.put("terminalId", terminalId);
        String currentUrl = manureHost + ManureUrl.isOnline;
        String url = getUrl(currentUrl, map);
        String result = HttpClientTools.get(url);
        System.out.println(result);
        if (!StringUtils.isEmpty(result)) {
            ResultObjectDTO data = new ObjectMapper().readValue(result, ResultObjectDTO.class);
            assertTrue(data.getStateCode() == 200);
        }
        fail("接口调用返回为null");
    }

    @Test
    public void testTerminalControlReq() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoteControlReqRemoteControlReqDTO() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoteControlReqTerminalControlReqDTO() {
        fail("Not yet implemented");
    }

    @Test
    public void testControl() {
        fail("Not yet implemented");
    }

    @Test
    public void testReadRealData() {
        fail("Not yet implemented");
    }

    @Test
    public void testObjectToMap() {
        fail("Not yet implemented");
    }

    private String getUrl(String url, HashMap<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    public static Map<String, String> objectToMap(Object obj) throws Exception {
        if (obj == null)
            return null;
        Map<String, String> map = new HashMap<String, String>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, (String) value);
        }

        return map;
    }
}
