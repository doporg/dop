package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
import com.clsaa.dop.server.code.model.vo.commit.CommitVo;
import com.clsaa.dop.server.code.service.CommitService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
    public List<CommitVo> findCommitList(@ApiParam(value = "项目id") @PathVariable("id") String id,
                                         @ApiParam(value = "路径") @RequestParam("path")String path,
                                         @ApiParam(value = "分支名或tag名")@RequestParam("ref_name")String ref_name,
                                         @ApiParam(value = "用户id") @RequestParam("userId") Long userId){

        List<CommitBo> commitBos;
        List<CommitVo> commitVos=new ArrayList<>();

        try {
            commitBos = commitService.findCommitList(id,path,ref_name,userId);
            for(CommitBo commitBo:commitBos){
                commitVos.add(BeanUtils.convertType(commitBo,CommitVo.class));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return commitVos;
    }
}
