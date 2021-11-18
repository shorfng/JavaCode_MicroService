package com.loto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * config 配置中心的配置信息
 */
@RestController
@RequestMapping("/config")
@RefreshScope  // 配置自动刷新
public class ConfigController {
    @Value("${mysql.url}")
    private String mysqlUrl;

    @Value("${td.message}")
    private String tdMessage;

    //http://localhost:8080/config/viewconfig
    @GetMapping("/viewconfig")
    public String viewconfig() {
        return "tdMessage==>" + tdMessage + " mysqlUrl==>" + mysqlUrl;
    }

    @Value("${abc.test}")
    private String abctest;

    @Value("${def.test}")
    private String deftest;

    //http://localhost:8080/config/dataids
    @GetMapping("/dataids")
    public String dataids() {
        return "abctest==>" + abctest + " deftest==>" + deftest;
    }
}
