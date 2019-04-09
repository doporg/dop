package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.file.BranchAndTagBo;
import com.clsaa.dop.server.code.model.bo.file.ChildrenBo;
import com.clsaa.dop.server.code.model.bo.file.TreeCommitBo;
import com.clsaa.dop.server.code.model.bo.file.TreeNodeBo;
import com.clsaa.dop.server.code.model.dto.file.FileUpdateDto;
import com.clsaa.dop.server.code.model.vo.file.*;
import com.clsaa.dop.server.code.service.FileService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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


    @ApiOperation(value = "查询项目文件结构",notes = "根据项目的id、分支或tag和查找指定路径下的node内容")
    @GetMapping("/projects/{id}/repository/tree/nodes")
    public List<TreeNodeVo> findTreeNodes(@ApiParam(value = "项目id") @PathVariable("id") String id,
                                     @ApiParam(value = "分支或tag名") @RequestParam("ref")String ref,
                                     @ApiParam(value = "路径") @RequestParam("path")String path,
                                     @ApiParam(value = "用户id") @RequestParam("userId") Long userId){

        List<TreeNodeVo> treeNodeVos=new ArrayList<>();

        try {
            List<TreeNodeBo> treeNodeBos = fileService.findTreeNodes(id,ref,path,userId);
            for(TreeNodeBo treeNodeBo:treeNodeBos){
                treeNodeVos.add(BeanUtils.convertType(treeNodeBo,TreeNodeVo.class));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return treeNodeVos;
    }


    @ApiOperation(value = "查询项目文件结构",notes = "根据项目的id、分支或tag和查找指定路径下的commit内容")
    @GetMapping("/projects/{id}/repository/tree/commits")
    public List<TreeCommitVo> findTreeCommits(@ApiParam(value = "项目id") @PathVariable("id") String id,
                                             @ApiParam(value = "分支或tag名") @RequestParam("ref")String ref,
                                             @ApiParam(value = "路径") @RequestParam("path")String path,
                                             @ApiParam(value = "用户id") @RequestParam("userId") Long userId){

        List<TreeCommitVo> treeCommitVos=new ArrayList<>();

        try {
            List<TreeCommitBo> treeCommitBos = fileService.findTreeCommits(id,ref,path,userId);
            for(TreeCommitBo treeCommitBo:treeCommitBos){
                treeCommitVos.add(BeanUtils.convertType(treeCommitBo,TreeCommitVo.class));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return treeCommitVos;
    }

    @ApiOperation(value = "查找项目的分支名和tag名",notes = "根据项目id查找项目的分支名和tag名")
    @GetMapping("/projects/{id}/repository/branchandtag")
    public List<BranchAndTagVo> findBranchAndTag(@ApiParam(value = "项目id")@PathVariable("id") String id,
                                                 @ApiParam(value = "用户id") @RequestParam("userId") Long userId){

//        long t1=System.currentTimeMillis();

        List<BranchAndTagBo> bos=fileService.findBranchAndTag(id,userId);
        List<BranchAndTagVo> vos=new ArrayList<>();

        List<ChildrenVo> branches=new ArrayList<>();
        List<ChildrenVo> tags=new ArrayList<>();

        for(ChildrenBo childrenBo:bos.get(0).getChildren()){
            branches.add(BeanUtils.convertType(childrenBo,ChildrenVo.class));
        }

        for(ChildrenBo childrenBo:bos.get(1).getChildren()){
            tags.add(BeanUtils.convertType(childrenBo,ChildrenVo.class));
        }

        BranchAndTagVo vo1=new BranchAndTagVo("branch","branch",branches);
        BranchAndTagVo vo2=new BranchAndTagVo("tag","tag",tags);

        vos.add(vo1);
        vos.add(vo2);

//        long t2=System.currentTimeMillis();
//
//        System.out.println("total:"+(t2-t1));

        return vos;

    }

    @ApiOperation(value = "查找文件内容",notes = "根据项目id，文件路径，ref查找文件内容")
    @GetMapping("/projects/{id}/repository/blob")
    public BlobVo findFileContent(@ApiParam(value = "项目id")@PathVariable("id") String id,
                                  @ApiParam(value = "文件路径")@RequestParam("file_path") String file_path,
                                  @ApiParam(value = "branch,tag or commit")@RequestParam("ref") String ref,
                                  @ApiParam(value = "用户id") @RequestParam("userId") Long userId){


        BlobVo bolbVo=null;

        try {
            bolbVo=BeanUtils.convertType(fileService.findFileContent(id,file_path,ref,userId),BlobVo.class) ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bolbVo;

    }

    @ApiOperation(value = "更新文件内容",notes = "更新文件内容，并作为一次提交")
    @PutMapping("/projects/{id}/repository/blob")
    public void updateFile(@ApiParam(value = "项目id") @PathVariable("id")String id,
                           @ApiParam(value = "文件更新内容") @RequestBody FileUpdateDto fileUpdateDto){

        try {
            fileService.updateFile(
                    id,
                    fileUpdateDto.getFile_path(),
                    fileUpdateDto.getBranch(),
                    fileUpdateDto.getContent(),
                    fileUpdateDto.getCommit_message(),
                    fileUpdateDto.getUserId());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "删除文件",notes = "删除文件，并作为一次提交")
    @DeleteMapping("/projects/{id}/repository/blob")
    public void deleteFile(@ApiParam(value = "项目id")@PathVariable("id") String id,
                           @ApiParam(value = "文件路径")@RequestParam("file_path") String file_path,
                           @ApiParam(value = "分支")@RequestParam("branch") String branch,
                           @ApiParam(value = "提交信息")@RequestParam("commit_message") String commit_message,
                           @ApiParam(value = "用户id") @RequestParam("userId") Long userId){

        try {
            fileService.deleteFile(id,file_path,branch,commit_message,userId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @ApiOperation(value = "查找项目所有的文件路径",notes = "根据项目id，分支或tag查找项目所有的文件的路径")
    @GetMapping("/projects/{id}/repository/filepathlist")
    public List<String> findAllFilePath(@ApiParam(value = "项目id")@PathVariable("id") String id,
                                        @ApiParam(value = "分支或tag")@RequestParam("ref") String ref,
                                        @ApiParam(value = "用户id") @RequestParam("userId") Long userId){
        return fileService.findAllFilePath(id,ref,userId);
    }



}
