package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.harborfeign.HarborRepoFeign;
import com.clsaa.dop.server.image.model.bo.RepositoryBO;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 镜像仓库服务类
 * @author  xzt
 * @since 2019-3-30
 */
@Service
public class RepositoryService {

    @Autowired
    private HarborRepoFeign harborRepoFeign;

    /**
     * 获取对应的仓库信息
     * @param projectId 项目id
     * @param q 检索条件
     * @param sort 排序方式
     * @param labelId 标签id
     * @param page 页号
     * @param pageSize 页大小
     * @param userId 用户id
     * @return {@link List<RepositoryBO>} 镜像仓库的列表
     */
    public List<RepositoryBO> getRepositories(Integer projectId, String q, String sort, Integer labelId, Integer page, Integer pageSize, Long userId){
        String auth = BasicAuthUtil.createAuth(userId);
        return BeanUtils.convertList(harborRepoFeign.repositoriesGet(projectId,q,sort,labelId,page,pageSize,auth),RepositoryBO.class);
    }

    /**
     *
     * @param projectId 项目id
     * @param repoName 镜像仓库名称
     * @param userId 用户id
     */
    public void deleteRepository(Integer projectId,String repoName,Long userId){

    }
}
