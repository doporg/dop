package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import org.springframework.stereotype.Service;

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
     * @return 提交列表
     */
    public List<CommitBo> findCommitList(int id,String username,String ref_name){

        List<CommitBo> commitBos= RequestUtil.getList("/projects/"+id+"/repository/commits?ref_name="+ref_name,username,CommitBo.class);
        for(CommitBo commitBo:commitBos){
            List<String> strs=TimeUtil.natureTime(commitBo.getAuthored_date());
            commitBo.setAuthored_date(strs.get(0));
            commitBo.setAuthored_time(strs.get(1));
        }

        return commitBos;

    }

}
