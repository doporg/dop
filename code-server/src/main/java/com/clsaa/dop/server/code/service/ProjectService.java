package com.clsaa.dop.server.code.service;


import com.clsaa.dop.server.code.model.bo.BranchBo;
import com.clsaa.dop.server.code.model.bo.ProjectBo;
import com.clsaa.dop.server.code.model.bo.ProjectListBo;
import com.clsaa.dop.server.code.model.bo.TagBo;
import com.clsaa.dop.server.code.model.vo.ProjectVo;
import com.clsaa.dop.server.code.util.BeanUtils;
import com.clsaa.dop.server.code.util.RequestUtil;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * gitlab项目服务类
 * @author wsy
 */
@Service
public class ProjectService {

    @Autowired
    private UserService userService;

    /**
     * 根据id查找项目
     * @param id 项目id
     * @return 项目overview需要的信息
     * @author wsy
     */
    public ProjectBo findProject(int id){
        //获得项目基本信息
        ProjectBo projectBo=RequestUtil.get("/projects/"+id+"?statistics=true",ProjectBo.class);
        //获得tag的数量
        List<TagBo> tags=RequestUtil.getList("/projects/"+id+"/repository/tags",TagBo.class);
        projectBo.setTag_count(tags.size());
        //获得分支的数量
        List<BranchBo> branches=RequestUtil.getList("/projects/"+id+"/repository/branches",BranchBo.class);
        projectBo.setBranch_count(branches.size());
        //获得提交次数
        projectBo.setCommit_count(projectBo.getStatistics().getCommit_count());
        //计算文件大小
        int storage_size=projectBo.getStatistics().getStorage_size();
        if(storage_size<1024){
            projectBo.setFile_size(storage_size+"B");
        }else if(storage_size<(1024*1024)){
            projectBo.setFile_size(storage_size/1024+"KB");
        }else if(storage_size<(1024*1024*1024)){
            projectBo.setFile_size(storage_size/(1024*1024)+"MB");
        }else {
            projectBo.setFile_size(storage_size/(1024*1024*1024)+"GB");
        }

        return projectBo;
    }

    /**
     * 以username的身份star一个project,若已经star，则unstar
     * @param id 项目id
     * @param username 用户
     */
    public void starProject(int id,String username){

        List<NameValuePair> params=new ArrayList<>();
        int code=RequestUtil.post("/projects/"+id+"/star",username,params);
        if(code==304){
            RequestUtil.post("/projects/"+id+"/unstar",username,params);
        }

    }



    /**
     * 查找用户参与的项目
     * @param username 用户名
     * @return 项目列表
     */
    public List<ProjectListBo> findProjectByMember(String username){

        //获得项目基本信息
        List<ProjectListBo> listBos=RequestUtil.getList("/projects?membership=true",username,ProjectListBo.class);

        return listBos;
    }

    public static void main(String[] args) {
        ProjectService ps=new ProjectService();
        ProjectBo projectBo=ps.findProject(3);
        ProjectVo projectVo=BeanUtils.convertType(projectBo,ProjectVo.class);

        System.out.println(projectVo);
    }

}
