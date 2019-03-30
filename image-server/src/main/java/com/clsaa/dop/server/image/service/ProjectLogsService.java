package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.AccessLogBO;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<AccessLogBO> getProjectLogs(Long projectId,String userName,String repo,String tag,
                                            String operation,String beginTime,String endTime,Integer page,Integer pageSize,Long userId){
        String auth = BasicAuthUtil.createAuth(userId);
        return BeanUtils.convertList(projectFeign.projectsProjectIdLogsGet(projectId,userName,repo,tag,operation,beginTime,endTime,page,pageSize,auth),AccessLogBO.class);
    }

}
