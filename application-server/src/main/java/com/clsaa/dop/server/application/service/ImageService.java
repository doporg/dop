package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.ImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "ImageService")
public class ImageService {
    @Autowired
    private ImageFeign imageFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createProject(String projectName, String projectStatus, Long userId) {
        logger.info("[createProject] Request coming: projectName={}, projectStatus={}, userId={}",projectName,projectStatus,userId);
        this.imageFeign.addProject(projectName, projectStatus, userId);
    }

    public List<String> getImageUrls(Long userId) {
        logger.info("[getImageUrls] Request coming: userId={}",userId);
        return this.imageFeign.getRepoAddress(userId);
    }
}
