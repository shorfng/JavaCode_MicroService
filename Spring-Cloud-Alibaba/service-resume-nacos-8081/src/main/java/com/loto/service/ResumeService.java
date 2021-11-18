package com.loto.service;

import com.loto.pojo.Resume;

public interface ResumeService {
    Resume findDefaultResumeByUserId(Long userId);
}
