package com.loto.service.impl;

import com.loto.ResumeService;
import com.loto.dao.ResumeDao;
import com.loto.pojo.Resume;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

// import org.apache.dubbo.config.annotation.Service;
@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeDao resumeDao;

    @Override
    public Integer findDefaultResumeByUserId(Long userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setIsDefault(1);    // 查询默认简历

        Example<Resume> example = Example.of(resume);
        //return resumeDao.findOne(example).get();

        return 8082;
    }
}
