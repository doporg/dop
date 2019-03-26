package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
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
public class CommitService {

    /**
     * 查看项目所有提交的列表
     * @param id 项目id
     * @param username 用户名
     * @param ref_name tag名或分支名
     * @param path 路径(需要urlencode)
     * @return 提交列表
     */
    public List<CommitBo> findCommitList(int id,String path,String ref_name,String username) throws UnsupportedEncodingException {

        String url="/projects/"+id+"/repository/commits?ref_name="+ref_name;
        //若不为根目录
        if(!path.equals("/")){
            url+="&path="+URLEncoder.encode(path,"GBK");
        }

        //取出所有的分页
        List<CommitBo> commitBos=new ArrayList<>();
        int page=1;
        while(true) {
            String temp_url=url+"&per_page=50&page="+page++;
            List<CommitBo> temp = RequestUtil.getList(temp_url, username, CommitBo.class);
            if(temp.size()==0) {
                System.out.println("break!!!!!");
                break;
            }else {
                commitBos.addAll(temp);
            }
        }


        for(CommitBo commitBo:commitBos){
            List<String> strs=TimeUtil.natureTime(commitBo.getAuthored_date());
            commitBo.setAuthored_date(strs.get(0));
            commitBo.setAuthored_time(strs.get(1));
        }

        return commitBos;

    }

}
