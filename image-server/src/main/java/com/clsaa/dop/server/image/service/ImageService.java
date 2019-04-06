package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborRepoFeign;
import com.clsaa.dop.server.image.model.bo.ImageInfoBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.DetailedTag;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    private final HarborRepoFeign harborRepoFeign;

    private final UserFeign userFeign;

    @Autowired
    public ImageService(HarborRepoFeign harborRepoFeign, UserFeign userFeign) {
        this.harborRepoFeign = harborRepoFeign;
        this.userFeign = userFeign;
    }


    /**
     * 获取镜像仓库的镜像信息
     * @param repoName 镜像仓库名称
     * @param labels 标签
     * @param userId 访问用户名
     * @return {@link List<ImageInfoBO>} 镜像信息的list
     */
    public List<ImageInfoBO> getImages(String projectName,String repoName, String labels, Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        return BeanUtils.convertList(harborRepoFeign.repositoriesRepoNameTagsGet(repo,labels,auth),ImageInfoBO.class);
    }

    /**
     * 通过镜像仓库名和tag名来删除镜像
     * @param projectName 项目名称
     * @param repoName  镜像仓库名称
     * @param tagName 镜像的tag名
     * @param userId   用户id
     */
    public void deleteImage(String projectName,String repoName,String imageName,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        harborRepoFeign.repositoriesRepoNameTagsTagDelete(repo,imageName,auth);
    }
}
