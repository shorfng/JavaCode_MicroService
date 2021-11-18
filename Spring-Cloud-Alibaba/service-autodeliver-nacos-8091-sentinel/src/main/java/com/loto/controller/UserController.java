package com.loto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务限流：QPS - 关联 - 快速失败
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 用户注册接口
     */
    @GetMapping("/register")
    public String register() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd HH:MM:ss");
        System.out.println(simpleDateFormat.format(new Date()) + " Register success!");
        return "Register success!";
    }


    /**
     * 验证注册身份证接口（需要调用公安户籍资源）
     */
    @GetMapping("/validateID")
    public String findResumeOpenState() {
        System.out.println("validateID");
        return "ValidateID success!";
    }

}
