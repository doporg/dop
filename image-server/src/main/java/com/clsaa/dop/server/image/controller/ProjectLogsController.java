package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.vo.AccessLogVO;
import com.clsaa.dop.server.image.service.ProjectLogsService;
import com.clsaa.dop.server.image.util.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  项目日志的控制器类
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@RestController
@Api(value = "ProjectLogsController|一个项目日志的控制器类")
public class ProjectLogsController {
    private final ProjectLogsService projectLogsService;

    @Autowired
    public ProjectLogsController(ProjectLogsService projectLogsService) {
        this.projectLogsService = projectLogsService;
    }

    @ApiOperation(value = "根据项目id获取项目日志")
    @GetMapping(value = "/v1/projects/{projectId}/logs")
    public List<AccessLogVO> getProjectLogs(@ApiParam(value = "项目的id",required = true)@PathVariable(value = "projectId")Long projectId,
                                            @ApiParam(value = "用户名称") @RequestParam(value = "username", required = false) String username,
                                            @ApiParam(value = "仓库名称") @RequestParam(value = "repository", required = false) String repository,
                                            @ApiParam(value = "标签号") @RequestParam(value = "tag", required = false) String tag,
                                            @ApiParam(value = "操作类型") @RequestParam(value = "operation", required = false) String operation,
                                            @ApiParam(value = "开始时间") @RequestParam(value = "beginTimestamp", required = false) String beginTimestamp,
                                            @ApiParam(value = "结束时间") @RequestParam(value = "endTimestamp", required = false) String endTimestamp,
                                            @ApiParam(value = "页号") @RequestParam(value = "page", required = false) Integer page,
                                            @ApiParam(value = "页大小") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                            @ApiParam(value = "用户id") @RequestHeader(value = "userId")Long userId){
        return BeanUtils.convertList(projectLogsService.getProjectLogs(projectId,username,repository,tag,operation,beginTimestamp,endTimestamp,page,pageSize,userId),AccessLogVO.class);
    }
}
