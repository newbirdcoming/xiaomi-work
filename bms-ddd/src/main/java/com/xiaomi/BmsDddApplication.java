package com.xiaomi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BmsDddApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(BmsDddApplication.class, args);
        String nameServer = context.getEnvironment().getProperty("spring.datasource.redis.host");
        String port = context.getEnvironment().getProperty("spring.datasource.redis.port");
        System.out.println("redis NameServer: " + nameServer+"redis Port: " + port);
    }

}
