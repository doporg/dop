package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.CodeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "CodeService")
public class CodeService {
    @Autowired
    private CodeFeign codeFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<String> findProjectUrlList(Long loginUser) {
        logger.info("[findProjectUrlList] Request coming: loginUser={}",loginUser);
        return this.codeFeign.findProjectUrlList(loginUser);

    }
}
