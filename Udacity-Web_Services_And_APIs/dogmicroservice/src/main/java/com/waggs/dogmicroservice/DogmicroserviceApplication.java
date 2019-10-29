package com.waggs.dogmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DogmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogmicroserviceApplication.class, args);
    }

}
