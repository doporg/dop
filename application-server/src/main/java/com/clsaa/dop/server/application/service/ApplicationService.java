package com.clsaa.dop.server.application.service;


import com.clsaa.dop.server.application.dao.ApplicationRepository;
import com.clsaa.dop.server.application.model.bo.AppBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.Project;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import javafx.application.Application;
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

@Service(value = "ApplicationService")
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * 通过projectId分页查询应用
     *
     * @param pageNo    页号
     * @param pageSize  页大小
     * @param projectId 项目Id
     * @param queryKey  查询关键字
     * @return {@link Pagination< AppBoV1>}
     */
    public Pagination<AppBoV1> findApplicationByProjectIdOrderByCtimeWithPage(Integer pageNo, Integer pageSize, Long projectId, String queryKey) {
        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);


        Page<App> applicationPage;
        List<App> applicationList;
        Integer totalCount;

        //如果带查询则使用上面的 带前缀查询的函数
        if (!queryKey.equals("")) {
            applicationPage = applicationRepository.findAllByProjectIdAndTitleStartingWith(projectId, queryKey, pageable);

            applicationList = applicationPage.getContent();
            totalCount = applicationRepository.findAllByProjectIdAndTitleStartingWith(projectId, queryKey).size();
        } else {

            applicationPage = applicationRepository.findAllByProjectId(projectId, pageable);

            applicationList = applicationPage.getContent();

            totalCount = applicationRepository.findAllByProjectId(projectId).size();
            //applicationList = applicationPage.getContent();

        }

        //新建BO层对象 并赋值
        Pagination<AppBoV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        if (applicationList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        pagination.setPageList(applicationList.stream().map(l -> BeanUtils.convertType(l, AppBoV1.class)).collect(Collectors.toList()));

        //for (Application item : applicationList) {
        //    ApplicationBoV1List.add(BeanUtils.convertType(item, AppBoV1.class));
        //}

        return pagination;


    }

    /**
     * 创建应用
     *
     * @param projectId   项目Id
     * @param title       应用名称
     * @param description 应用描述
     * @return {@link Pagination< AppBoV1>}
     */
    public void createApp(Long projectId, String title, String description) {
        Long cuser = 1234L;
        Long muser = 1234L;
        Long ouser = 1111L;
        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        App app = App.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .ouser(ouser)
                .is_deleted(false)
                .ctime(ctime)
                .mtime(mtime)
                .build();
        this.applicationRepository.saveAndFlush(app);
        return;
    }
}
