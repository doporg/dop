package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.feign.UserInterface;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceExecuteLogDto;
import com.clsaa.dop.server.test.model.dto.OperationExecuteLogDto;
import com.clsaa.dop.server.test.model.dto.User;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Component
@Slf4j
public class InterfaceExecuteLogDtoMapper extends AbstractCommonServiceMapper<InterfaceExecuteLog, InterfaceExecuteLogDto> {

    @Autowired
    private OperationLogDtoMapper operationLogDtoMapper;

    @Autowired
    private UserInterface userInterface;

    private LoadingCache<Long,String> userNameCache = CacheBuilder.newBuilder().maximumSize(10000L).build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long key) throws Exception {
            User user = userInterface.getUserById(key);
            if (user == null) {
                log.error("[Get User Info From Feign]: error! invalid user id: {}", key);
                return "Invalid UserId " + key;
//                BizAssert.justInvalidParam("[Get User Info From Feign]: error! invalid user id: {}" + key);
            }
            return user.getName();
        }
    });

    @Override
    public Class<InterfaceExecuteLog> getSourceClass() {
        return InterfaceExecuteLog.class;
    }

    @Override
    public Class<InterfaceExecuteLogDto> getTargetClass() {
        return InterfaceExecuteLogDto.class;
    }

    @Override
    public Optional<InterfaceExecuteLogDto> convert(InterfaceExecuteLog interfaceExecuteLog) {
        return super.convert(interfaceExecuteLog).map(interfaceExecuteLogDto -> {
            List<OperationExecuteLog> logs = interfaceExecuteLog.getOperationExecuteLogs();
            Map<Stage, List<OperationExecuteLog>> groups = logs.stream()
                    .sorted(Comparator.comparingInt(OperationExecuteLog::getOrder))
                    .collect(Collectors.groupingBy(OperationExecuteLog::getStage));

            List<OperationExecuteLog> sortedLog = new ArrayList<>();
            sortedLog.addAll(groups.getOrDefault(Stage.PREPARE, new ArrayList<>()));
            sortedLog.addAll(groups.getOrDefault(Stage.TEST, new ArrayList<>()));
            sortedLog.addAll(groups.getOrDefault(Stage.DESTROY, new ArrayList<>()));

            List<OperationExecuteLogDto> logDtos = operationLogDtoMapper.convert(sortedLog);
            interfaceExecuteLogDto.setOperationExecuteLogs(logDtos);

            // user name
            Long cuser = interfaceExecuteLogDto.getCuser();
            try {
                interfaceExecuteLogDto.setCreateUserName(userNameCache.get(cuser));
            } catch (ExecutionException e) {
                log.error("Get user name error! userId: {}", cuser, e);
            }

            return interfaceExecuteLogDto;
        });
    }
}
