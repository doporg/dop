package com.clsaa.dop.server.application.service;


import com.clsaa.dop.server.application.dao.AppRepository;
import com.clsaa.dop.server.application.model.bo.AppBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnv;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.model.vo.AppV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppService")
public class AppService {
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppUrlInfoService appUrlInfoService;
    @Autowired
    private AppEnvService appEnvService;

    /**
     * 通过projectId分页查询应用
     *
     * @param pageNo    页号
     * @param pageSize  页大小
     * @param projectId 项目Id
     * @param queryKey  查询关键字
     * @return {@link Pagination< AppBoV1>}
     */
    public Pagination<AppV1> findApplicationByProjectIdOrderByCtimeWithPage(Integer pageNo, Integer pageSize, Long projectId, String queryKey) {
        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);


        Page<App> applicationPage;
        List<App> applicationList;
        Integer totalCount;

        //如果带查询则使用上面的 带前缀查询的函数
        if (!queryKey.equals("")) {
            applicationPage = appRepository.findAllByProjectIdAndTitleStartingWith(projectId, queryKey, pageable);

            applicationList = applicationPage.getContent();
            totalCount = appRepository.findAllByProjectIdAndTitleStartingWith(projectId, queryKey).size();
        } else {

            applicationPage = appRepository.findAllByProjectId(projectId, pageable);

            applicationList = applicationPage.getContent();

            totalCount = appRepository.findAllByProjectId(projectId).size();
            //applicationList = applicationPage.getContent();

        }

        //新建BO层对象 并赋值
        Pagination<AppV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        if (applicationList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        pagination.setPageList(applicationList.stream().map(l -> BeanUtils.convertType(l, AppV1.class)).collect(Collectors.toList()));

        return pagination;


    }

    /**
     * 删除应用
     *
     * @param id appId
     */
    public void deleteApp(Long id) {
        this.appRepository.deleteById(id);
        //appUrlInfoService.deleteAppUrlInfo(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }

    /**
     * 删除应用
     *
     * @param id appId
     */
    public void updateApp(Long id, String description) {
        App app = this.appRepository.findById(id).orElse(null);
        app.setDescription(description);
        this.appRepository.saveAndFlush(app);
        //appUrlInfoService.deleteAppUrlInfo(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }



    /**
     * 创建应用
     *
     * @param projectId   项目Id
     * @param title       应用名称
     * @param description 应用描述
     *
     */
    public void createApp(Long cuser, Long projectId, String title, String description, String productMode, String gitUrl) {

        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        App app = App.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .cuser(cuser)
                .muser(cuser)
                .ouser(cuser)

                .is_deleted(false)
                .ctime(ctime)
                .mtime(mtime)
                .productMode(App.ProductMode.valueOf(productMode))
                .build();
        this.appRepository.saveAndFlush(app);

        Long appId = app.getId();

        AppEnv appEnv = AppEnv.builder()
                .appId(appId)
                .ctime(ctime)
                .cuser(cuser)
                .is_deleted(false)
                .mtime(mtime)
                .muser(cuser)
                .deploymentStrategy(AppEnv.DeploymentStrategy.KUBERNETES)
                .title("日常开发")
                .environmentLevel(AppEnv.EnvironmentLevel.DAILY)
                .build();
        this.appEnvService.createAppEnv(appEnv);

        AppUrlInfo appUrlInfo = AppUrlInfo.builder()
                .appId(appId)
                .ctime(ctime)
                .cuser(cuser)
                .is_deleted(false)
                .mtime(mtime)
                .muser(cuser)
                .warehouseUrl(gitUrl)
                .build();
        this.appUrlInfoService.createAppUrlInfo(appUrlInfo);

        return;
    }

    /**
     * 查询应用
     *
     * @param id id
     * @return @return {@link AppBoV1}
     */
    public AppBoV1 findAppById(Long id) {
        System.out.print(this.appRepository.findById(id).orElse(null));
        return BeanUtils.convertType(this.appRepository.findById(id).orElse(null), AppBoV1.class);

    }
}
