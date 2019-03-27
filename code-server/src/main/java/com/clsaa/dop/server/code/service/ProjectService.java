package com.clsaa.dop.server.code.service;


import com.clsaa.dop.server.code.model.bo.project.BranchBo;
import com.clsaa.dop.server.code.model.bo.project.ProjectBo;
import com.clsaa.dop.server.code.model.bo.project.ProjectListBo;
import com.clsaa.dop.server.code.model.bo.project.TagBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    public List<ProjectListBo> findProjectList(String sort,String username){

        List<ProjectListBo> listBos;

        if(sort.equals("personal")) {
            listBos=RequestUtil.getList("/projects?membership=true&order_by=updated_at",username,ProjectListBo.class);
        }else if(sort.equals("starred")){
            listBos=RequestUtil.getList("/projects?starred=true&order_by=updated_at",username,ProjectListBo.class);
        }else {
            listBos=RequestUtil.getList("/projects?visibility=public&order_by=updated_at",username,ProjectListBo.class);
        }

        return listBos;
    }

    /**
     * 新建一个gitlab project
     * @param name 项目名称
     * @param description 项目描述
     * @param visibility 可见等级
     * @param initialize_with_readme 是否新建readme文件
     * @param username 项目owner用户名
     */
    public void addProject(String name,String description,String visibility,String initialize_with_readme,String username){

        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("description",description));
        params.add(new BasicNameValuePair("visibility",visibility));
        params.add(new BasicNameValuePair("initialize_with_readme",initialize_with_readme));

        RequestUtil.post("/projects",username,params);


    }


}
