package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.loto.pojo")
public class ResumeApplication8080 {
    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication8080.class,args);
    }
}
