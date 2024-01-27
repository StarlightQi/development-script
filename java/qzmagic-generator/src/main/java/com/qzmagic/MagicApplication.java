package com.qzmagic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qzmagic.mapper")
public class MagicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MagicApplication.class, args);
    }
}