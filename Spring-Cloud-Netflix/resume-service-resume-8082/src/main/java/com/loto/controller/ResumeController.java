package com.loto.controller;

import com.loto.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：蓝田_Loto
 * <p>Date：2021-11-05 19:30</p>
 * <p>PageName：ResumeController.java</p>
 * <p>Function：</p>
 */

@RestController
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @Value("${server.port}")
    private Integer port;

    //http://localhost:8080/resume/openstate/1545132
    @GetMapping("/openstate/{userId}")
    public Integer findDefaultResumeState(@PathVariable Long userId) {
        //return resumeService.findDefaultResumeByUserId(userId).getIsOpenResume();
        return port;
    }
}
