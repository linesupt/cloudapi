package com.lineying;

import com.lineying.common.SecureConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.logging.Logger;

@SpringBootApplication
@MapperScan(basePackages = "com.lineying.mapper")
public class CloudApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CloudApplication.class);
    }

}
