package com.chen.stencil.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chen"})
public class StencilUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(StencilUserApplication.class, args);
    }

}
