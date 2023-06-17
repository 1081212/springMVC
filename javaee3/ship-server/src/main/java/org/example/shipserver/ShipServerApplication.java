package org.example.shipserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //开启nacos服务注册，autoRegister默认为true，注册到服务中心
@EnableFeignClients //开启Feign
public class ShipServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipServerApplication.class, args);
    }

}