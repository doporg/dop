package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.AccessLogBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.AccessLog;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.dop.server.image.util.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     项目日志的业务实现
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Service
public class ProjectLogsService {

    @Autowired
    private ProjectFeign projectFeign;
    @Autowired
    private UserFeign userFeign;

    /**
     * 通过参数来对项目的日志进行检索
     * @param projectId 项目id
     * @param userName 用户名
     * @param repo 仓库名称
     * @param tag 标签
     * @param operation 操作类型
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param page 页号
     * @param pageSize 页大小
     * @return {@link List<AccessLogBO>}根据参数检索项目日志
     */
    public List<AccessLogBO> getProjectLogs(Integer projectId,String userName,String repo,String tag,
                                            String operation,String beginTime,String endTime,Integer page,Integer pageSize,Long userId){
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        List<AccessLog> accessLogs = projectFeign.projectsProjectIdLogsGet(projectId,userName,repo,tag,operation,beginTime,
                endTime,page,pageSize,auth);
        List<AccessLogBO> accessLogBOS = new ArrayList<>();
        for (AccessLog accessLog:accessLogs){
            AccessLogBO accessLogBO = BeanUtils.convertType(accessLog,AccessLogBO.class);
            accessLogBO.setOpTime(TimeConvertUtil.convertTime(accessLog.getOpTime()));
            accessLogBOS.add(accessLogBO);
        }
        return accessLogBOS;
    }

}
