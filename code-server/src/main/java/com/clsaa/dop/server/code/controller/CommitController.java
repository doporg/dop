package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
import com.clsaa.dop.server.code.model.vo.commit.CommitVo;
import com.clsaa.dop.server.code.service.CommitService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@RestController
@CrossOrigin
public class CommitController {

    @Autowired
    private CommitService commitService;

    @ApiOperation(value = "查看项目的提交列表",notes = "根据项目的id查看项目的提交列表")
    @GetMapping("/projects/{id}/repository/commits")
    public List<CommitVo> findCommitList(@ApiParam(value = "项目id") @PathVariable("id") int id,
                                         @ApiParam(value = "用户名") @RequestParam("username") String username,
                                         @ApiParam(value = "分支名或tag名")@RequestParam("ref_name")String ref_name){
        List<CommitBo> commitBos=commitService.findCommitList(id,username,ref_name);
        List<CommitVo> commitVos=new ArrayList<>();
        for(CommitBo commitBo:commitBos){
            commitVos.add(BeanUtils.convertType(commitBo,CommitVo.class));
        }

        return commitVos;
    }
}
