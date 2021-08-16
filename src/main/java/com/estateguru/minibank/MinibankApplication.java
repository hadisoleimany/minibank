package com.estateguru.minibank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class MinibankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinibankApplication.class, args);
    }

}
