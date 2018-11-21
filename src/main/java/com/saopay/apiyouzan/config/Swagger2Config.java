package com.saopay.apiyouzan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author huangding
 * @description
 * @date 2018/9/28 9:12
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.host}")
    private String host;


    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).host(host).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.saopay.apiyouzan"))
            .paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("有赞对接").contact(new Contact("huangding", "", ""))
            .version("1.0").description("工具").build();

    }


}
