package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.file.*;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@Service
public class FileService {


    /**
     * 查找路径下的tree node 节点
     * @param id 项目id
     * @param ref 分支名或tag名
     * @param path 文件路径
     * @return path下的所有文件和文件夹节点
     */
    public List<TreeNodeBo> findTree(int id, String ref, String path,String username) throws UnsupportedEncodingException {

        List<TreeNodeBo> treeNodeBos= RequestUtil.getList("/projects/"+id+"/repository/tree?ref="+ref+"&path="+URLEncoder.encode(path,"GBK") ,username,TreeNodeBo.class);

        for(TreeNodeBo treeNode:treeNodeBos){
            //获得最近提交的一次
            CommitBo commit=RequestUtil.getList("/projects/"+id+"/repository/commits?ref_name="+ref+"&path="+URLEncoder.encode(treeNode.getPath(),"GBK"),username,CommitBo.class).get(0);
            treeNode.setCommit_id(commit.getId());
            treeNode.setCommit_msg(commit.getMessage());
            List<String> res=TimeUtil.natureTime(commit.getCommitted_date());
            treeNode.setCommit_date(res.get(0));
            treeNode.setCommit_time(res.get(1));
        }

        return treeNodeBos;
    }


    /**
     * 查找项目所有的分支名和tag名
     * @param id 项目id
     * @param username 用户名
     * @return 分支名和tag名
     */
    public List<BranchAndTagBo> findBranchAndTag(int id, String username){

        List<BranchBo> branchBos= RequestUtil.getList("/projects/"+id+"/repository/branches",username,BranchBo.class);
        List<TagBo> tagBos=RequestUtil.getList("/projects/"+id+"/repository/tags",username,TagBo.class);

        List<ChildrenBo> branches=new ArrayList<>();
        List<ChildrenBo> tags=new ArrayList<>();

        for(BranchBo branchBo:branchBos){
            branches.add(new ChildrenBo(branchBo.getName(),branchBo.getName()));
        }

        for(TagBo tagBo:tagBos){
            tags.add(new ChildrenBo(tagBo.getName(),tagBo.getName()));
        }

        List<BranchAndTagBo> res=new ArrayList<>();
        res.add(new BranchAndTagBo("branch","branch",branches));
        res.add(new BranchAndTagBo("tag","tag",tags));

        return res;

    }
}
