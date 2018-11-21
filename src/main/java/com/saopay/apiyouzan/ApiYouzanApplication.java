package com.saopay.apiyouzan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.saopay.apiyouzan")
//mybatis
@MapperScan("com.saopay.apiyouzan.data.dao.mapper")
@EnableSwagger2
//缓存
@EnableCaching
public class ApiYouzanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiYouzanApplication.class, args);
    }
}
