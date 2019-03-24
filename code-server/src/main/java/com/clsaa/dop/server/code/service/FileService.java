package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.file.*;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Blob;
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

    /**
     * 根据分支和路径获得文件的内容
     * @param id 项目id
     * @param file_path 文件路径(原始路径，需要urlencode)
     * @param ref branch,tag or commit
     * @param username 用户名
     * @return 文件内容(raw)
     */
    public BlobBo findFileContent(int id, String file_path, String ref, String username) throws UnsupportedEncodingException {


        file_path=URLEncoder.encode(file_path,"GBK");

        String content=RequestUtil.getString("/projects/"+id+"/repository/files/"+file_path+"/raw?ref="+ref,username);

        BlobBo blobBo=RequestUtil.get("/projects/"+id+"/repository/files/"+file_path+"?ref="+ref,username,BlobBo.class);

        blobBo.setFile_content(content);

        int size=blobBo.getSize();

        if(size<1024){
            blobBo.setFile_size(size+"B");
        }else if(size<(1024*1024)){
            blobBo.setFile_size(size/1024+"KB");
        }else if(size<(1024*1024*1024)){
            blobBo.setFile_size(size/(1024*1024)+"MB");
        }else {
            blobBo.setFile_size(size/(1024*1024*1024)+"GB");
        }

        return blobBo;


    }


    /**
     * 更新文件并作为一次提交
     * @param id 项目id
     * @param file_path 文件路径
     * @param branch 分支
     * @param content 更新的文件内容
     * @param commit_message 提交信息
     * @param username 用户
     * @throws UnsupportedEncodingException
     */
    public void updateFile(int id,String file_path,String branch,String content,String commit_message,String username) throws UnsupportedEncodingException {


        NameValuePair p1=new BasicNameValuePair("branch",branch);
        NameValuePair p2=new  BasicNameValuePair("content",content);
        NameValuePair p3=new  BasicNameValuePair("commit_message",commit_message);

        List<NameValuePair> list=new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        String path="/projects/"+id+"/repository/files/"+URLEncoder.encode(file_path,"GBK");

        RequestUtil.put(path,username,list);
    }
}
