package com.clsaa.dop.server.image.feign.harborfeign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.Repository;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用于和harbor通讯的镜像仓库接口
 * @author xzt
 * @since 2019-3-29
 */
@FeignClient(url = "${feign.url}",value = "repo",configuration = FeignConfig.class)
public interface HarborRepoFeign {

    /**
     * 通过项目id检索仓库
     * @param projectId 项目id
     * @param q 通过name查询
     * @param sort 进行排序
     * @param labelId 标签id
     * @param page 页号，默认为1
     * @param pageSize 页大小默认为10，最大为100
     * @return {@link List<Repository>}  仓库列表
     */
    @GetMapping(value = "/repositories")
    List<Repository> repositoriesGet(@RequestParam(value = "project_id") Integer projectId,
                                     @RequestParam(value = "q", required = false) String q,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "label_id", required = false) Integer labelId,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "page_size", required = false) Integer pageSize);

    /**
     * 通过仓库名删除镜像仓库
     * @param repoName 仓库名称
     */
    @DeleteMapping(value = "/repositories/{repo_name}")
    void repositoriesRepoNameDelete(@PathVariable("repo_name") String repoName);

    @PutMapping()

}
