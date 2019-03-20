package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.file.TreeNodeBo;
import com.clsaa.dop.server.code.model.vo.file.TreeNodeVo;
import com.clsaa.dop.server.code.service.FileService;
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
@CrossOrigin
@RestController
public class FileController {

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "查询项目文件结构",notes = "根据项目的id、分支或tag和查找指定路径下的内容")
    @GetMapping("/projects/{id}/repository/tree")
    public List<TreeNodeVo> findTree(@ApiParam(value = "项目id") @PathVariable("id") int id,
                                     @ApiParam(value = "分支或tag名") @RequestParam("ref")String ref,
                                     @ApiParam(value = "路径") @RequestParam("path")String path){

        List<TreeNodeVo> treeNodeVos=new ArrayList<>();

        List<TreeNodeBo> treeNodeBos=fileService.findTree(id,ref,path);
        for(TreeNodeBo treeNodeBo:treeNodeBos){
            treeNodeVos.add(BeanUtils.convertType(treeNodeBo,TreeNodeVo.class));
        }

        return treeNodeVos;
    }

}
