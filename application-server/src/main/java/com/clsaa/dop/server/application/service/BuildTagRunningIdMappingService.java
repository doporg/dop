package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.BuildTagRunningIdMappingRepository;
import com.clsaa.dop.server.application.model.po.BuildTagRunningIdMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "BuildTagRunningIdMappingService")
public class BuildTagRunningIdMappingService {
    @Autowired
    BuildTagRunningIdMappingRepository buildTagRunningIdMappingRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String findBuildTagByRunningIdAndAppEnvId(Long cuser, String runningId, Long appEnvId) {
        logger.info("[findBuildTagByRunningIdAndAppEnvId] Request coming: cuser={}, runningId={}, appEnvId={}",cuser,runningId,appEnvId);
        BuildTagRunningIdMapping buildTagRunningIdMapping = this.buildTagRunningIdMappingRepository.findByRunningIdAndAppEnvId(runningId, appEnvId).orElse(null);
        if (buildTagRunningIdMapping == null) {
            return createMapping(cuser, appEnvId, runningId);
        } else {
            return buildTagRunningIdMapping.getBuildTag();
        }
    }

    public List<String> findRunningIdByAppEnvId(Long appEnvId) {
        logger.info("[findRunningIdByAppEnvId] Request coming: appEnvId={}",appEnvId);
        List<BuildTagRunningIdMapping> buildTagRunningIdMapping = this.buildTagRunningIdMappingRepository.findAllByAppEnvId(appEnvId);
        List<String> runningIdList = buildTagRunningIdMapping.stream().map(l -> l.getRunningId()).collect(Collectors.toList());
        return runningIdList;
    }

    public Long findAppEnvIdByRunningId(String runningId) {
        logger.info("[findAppEnvIdByRunningId] Request coming: runningId={}",runningId);
        BuildTagRunningIdMapping buildTagRunningIdMapping = this.buildTagRunningIdMappingRepository.findByRunningId(runningId).orElse(null);
        return buildTagRunningIdMapping.getAppEnvId();
    }

    public String createMapping(Long cuser, Long appEnvId, String runningId) {
        logger.info("[createMapping] Request coming: cuser={}, appEnvId={}, runningId={}",cuser,appEnvId,runningId);
        LocalDateTime now = LocalDateTime.now();
        BuildTagRunningIdMapping buildTagRunningIdMapping = BuildTagRunningIdMapping.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .appEnvId(appEnvId)
                .runningId(runningId)
                .buildTag(String.valueOf(now.getYear()) + String.valueOf(now.getMonthValue()) + String.valueOf(now.getDayOfMonth()) + String.valueOf(now.getHour()) + String.valueOf(now.getMinute()) + String.valueOf(appEnvId))
                .build();

        this.buildTagRunningIdMappingRepository.saveAndFlush(buildTagRunningIdMapping);
        return buildTagRunningIdMapping.getBuildTag();

    }


}
