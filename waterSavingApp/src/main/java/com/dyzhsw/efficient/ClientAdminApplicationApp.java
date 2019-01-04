package com.dyzhsw.efficient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lihd
 */
@Transactional
@SpringBootApplication
@EnableScheduling
@MapperScan({"com.dyzhsw.efficient.dao"})
public class ClientAdminApplicationApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientAdminApplicationApp.class, args);
    }
}
