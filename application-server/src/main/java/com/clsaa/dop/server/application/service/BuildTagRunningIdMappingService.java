package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.BuildTagRunningIdMappingRepository;
import com.clsaa.dop.server.application.model.po.BuildTagRunningIdMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "BuildTagRunningIdMappingService")
public class BuildTagRunningIdMappingService {
    @Autowired
    BuildTagRunningIdMappingRepository buildTagRunningIdMappingRepository;

    public String findBuildTagByRunningIdAndAppEnvId(Long cuser, Long runningId, Long appEnvId) {
        BuildTagRunningIdMapping buildTagRunningIdMapping = this.buildTagRunningIdMappingRepository.findByRunningIdAndAppEnvId(runningId, appEnvId).orElse(null);
        if (buildTagRunningIdMapping == null) {
            return createMapping(cuser, appEnvId, runningId);
        } else {
            return buildTagRunningIdMapping.getBuildTag();
        }
    }

    public String createMapping(Long cuser, Long appEnvId, Long runningId) {
        LocalDateTime now = LocalDateTime.now();
        BuildTagRunningIdMapping buildTagRunningIdMapping = BuildTagRunningIdMapping.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .appEnvId(appEnvId)
                .runningId(runningId)
                .buildTag(now.getYear() + now.getMonthValue() + now.getDayOfMonth() + now.getHour() + now.getMinute() + String.valueOf(appEnvId))
                .build();

        this.buildTagRunningIdMappingRepository.saveAndFlush(buildTagRunningIdMapping);
        return buildTagRunningIdMapping.getBuildTag();

    }


}
