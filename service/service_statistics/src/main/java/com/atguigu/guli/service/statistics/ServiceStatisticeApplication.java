package com.atguigu.guli.service.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan({"com.atguigu"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceStatisticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceStatisticeApplication.class, args);
    }
}
