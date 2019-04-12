package com.clsaa.dop.server.code.service;


import com.clsaa.dop.server.code.model.bo.project.*;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.URLUtil;
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


    /**
     * 根据id查找项目
     * @param id 项目id
     * @return 项目overview需要的信息
     * @author wsy
     */
    public ProjectBo findProject(String id){

        id=URLUtil.encode(id);

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
     * @param userId 用户id
     * @return 状态码
     */
    public int starProject(String id,Long userId){

        id=URLUtil.encode(id);

        List<NameValuePair> params=new ArrayList<>();
        int code=RequestUtil.post("/projects/"+id+"/star",userId,params);
        if(code==304){
            RequestUtil.post("/projects/"+id+"/unstar",userId,params);
        }

        return code;

    }



    /**
     * 查找用户参与的项目
     * @param userId 用户id
     * @return 项目列表
     */
    public List<ProjectListBo> findProjectList(String sort,Long userId){

        List<ProjectListBo> listBos;

        if(sort.equals("personal")) {
            listBos=RequestUtil.getList("/projects?membership=true&order_by=updated_at",userId,ProjectListBo.class);
        }else if(sort.equals("starred")){
            listBos=RequestUtil.getList("/projects?starred=true&order_by=updated_at",userId,ProjectListBo.class);
        }else {
            listBos=RequestUtil.getList("/projects?visibility=public&order_by=updated_at",userId,ProjectListBo.class);
        }

        return listBos;
    }

    /**
     * 新建一个gitlab project
     * @param name 项目名称
     * @param description 项目描述
     * @param visibility 可见等级
     * @param initialize_with_readme 是否新建readme文件
     * @param userId 项目owner用户id
     */
    public void addProject(String name,String description,String visibility,String initialize_with_readme,Long userId){

        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("description",description));
        params.add(new BasicNameValuePair("visibility",visibility));
        params.add(new BasicNameValuePair("initialize_with_readme",initialize_with_readme));

        RequestUtil.post("/projects",userId,params);


    }

    /**
     * 查找编辑项目需要的项目信息
     * @param id 项目id
     * @param userId 用户id
     * @return 项目信息
     */
    public ProjectEditBo findProjectEditInfo(String id,Long userId){

        id=URLUtil.encode(id);

        String path="/projects/"+id;
        return RequestUtil.get(path,userId,ProjectEditBo.class);

    }

    /**
     * 编辑项目信息
     * @param id 项目id
     * @param name 项目名称
     * @param description 项目描述
     * @param default_branch 默认分支
     * @param visibility 可见等级
     * @param userId 用户id
     */
    public void editProjectInfo(String id,String name,String description,String default_branch,String visibility,Long userId){

        id=URLUtil.encode(id);

        String path="/projects/"+id;
        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("description",description));
        params.add(new BasicNameValuePair("default_branch",default_branch));
        params.add(new BasicNameValuePair("visibility",visibility));
        RequestUtil.put(path,userId,params);
    }

    /**
     * 获得项目所有的分支名称
     * @param id 项目id
     * @param userId 用户id
     * @return 项目信息
     */
    public List<String> findAllBranchName(String id,Long userId){

        id=URLUtil.encode(id);

        List<BranchBo> branchBos= RequestUtil.getList("/projects/"+id+"/repository/branches",userId, BranchBo.class);
        List<String> res=new ArrayList<>();
        for(BranchBo branchBo:branchBos)
            res.add(branchBo.getName());
        return res;
    }


    /**
     * 删除一个项目
     * @param id 项目id
     * @param userId 用户id
     */
    public void deleteProject(String id,Long userId){

        id=URLUtil.encode(id);

        String path="/projects/"+id;
        RequestUtil.delete(path,userId);
    }


    /**
     * 获得项目的默认分支名
     * @param id 项目id
     * @param userId 用户id
     * @return 项目的默认分支名
     */
    public String findProjectDefaultBranch(String id,Long userId){

        id=URLUtil.encode(id);

        long t1=System.currentTimeMillis();
        DefaultBranchBo defaultBranchBo=RequestUtil.get("/projects/"+id,userId,DefaultBranchBo.class);
        long t2=System.currentTimeMillis();
        System.out.println("default branch:"+(t2-t1));
        return defaultBranchBo.getDefault_branch();
    }



}
