package com.work.commuity2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.work.commuity2.mapper")
public class Commuity2Application {

    public static void main(String[] args) {
        SpringApplication.run(Commuity2Application.class, args);
    }

}
