package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.file.CommitBo;
import com.clsaa.dop.server.code.model.bo.file.TreeNodeBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import org.springframework.stereotype.Service;

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
    public List<TreeNodeBo> findTree(int id, String ref, String path){

        List<TreeNodeBo> treeNodeBos= RequestUtil.getList("/projects/"+id+"/repository/tree?ref="+ref+"&path="+path,TreeNodeBo.class);

        for(TreeNodeBo treeNode:treeNodeBos){
            //获得最近提交的一次
            CommitBo commit=RequestUtil.getList("/projects/"+id+"/repository/commits?path="+treeNode.getPath(),CommitBo.class).get(0);
            treeNode.setCommit_id(commit.getId());
            treeNode.setCommit_msg(commit.getMessage());
            List<String> res=TimeUtil.natureTime(commit.getCommitted_date());
            treeNode.setCommit_date(res.get(0));
            treeNode.setCommit_time(res.get(1));
        }

        return treeNodeBos;
    }
}
