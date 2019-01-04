package com.dyzhsw.efficient;

import com.dyzhsw.efficient.service.SysService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author: pjx
 * @Date: 2018/12/13 16:16
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientAdminApplicationApp.class)
@WebAppConfiguration
public class BaseJunit {
    @Autowired
    WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;
    @Autowired
    protected SysService sysService;
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initDatabase() {
    }
}
