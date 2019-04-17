package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborRepoFeign;
import com.clsaa.dop.server.image.model.bo.RepositoryBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.Repository;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.dop.server.image.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 镜像仓库服务类
 * @author  xzt
 * @since 2019-3-30
 */
@Service
public class RepositoryService {

    private final HarborRepoFeign harborRepoFeign;

    private final UserFeign userFeign;

    @Autowired
    public RepositoryService(HarborRepoFeign harborRepoFeign, UserFeign userFeign) {
        this.harborRepoFeign = harborRepoFeign;
        this.userFeign = userFeign;
    }

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
    public Pagination<RepositoryBO> getRepositories(Integer projectId, String q, String sort, Integer labelId, Integer page, Integer pageSize, Long userId){
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);

        ResponseEntity<List<Repository>> responseEntity = harborRepoFeign.repositoriesGet(projectId,q,sort,labelId,page,pageSize,auth);
        List<Repository> repositories = responseEntity.getBody();
        System.out.println(repositories.size());

        int count = 0;
        List<String> httpHeader = responseEntity.getHeaders().get("X-Total-Count");
        if (httpHeader!=null){
            count = Integer.parseInt(httpHeader.get(0));
        }

        Pagination<RepositoryBO> pagination = new Pagination<>();
        pagination.setTotalCount(count);


        if (count==0){
            pagination.setContents(Collections.emptyList());
            return pagination;
        }else {
            List<RepositoryBO> repositoryBOS = new ArrayList<>();
            for (Repository repository:repositories){
                RepositoryBO repositoryBO = BeanUtils.convertType(repository,RepositoryBO.class);
                repositoryBOS.add(repositoryBO);
            }
            pagination.setContents(repositoryBOS);
            return pagination;
        }
    }

    /**
     *
     * @param projectName 项目id
     * @param repoName 镜像仓库名称
     * @param userId 用户id
     */
    public void deleteRepository(String projectName,String repoName,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        System.out.println(repo);
        harborRepoFeign.repositoriesRepoNameDelete(repo,auth);
    }
}
