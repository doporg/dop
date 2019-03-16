package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppVarRepository;
import com.clsaa.dop.server.application.model.bo.AppVarBoV1;
import com.clsaa.dop.server.application.model.po.AppVariable;
import com.clsaa.dop.server.application.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppVarService")
public class AppVarService {
    @Autowired
    AppVarRepository appVarRepository;


    /**
     * 创建变量
     *
     * @param cuser 创建者
     * @param appId 应用id
     * @param key   键
     * @param value 值
     */
    public void createAppVarByAppId(Long cuser, Long appId, String key, String value) {
        AppVariable appVariable = AppVariable.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .appId(appId)
                .varKey(key)
                .varValue(value)
                .build();
        this.appVarRepository.saveAndFlush(appVariable);
    }

    /**
     * 查找appId下所有变量
     *
     * @param appId 应用id
     * @return {@link List<AppVarBoV1>}
     */
    public List<AppVarBoV1> findAppVarByAppIdOrderByKey(Long appId) {
        Sort sort = new Sort(Sort.Direction.ASC, "varKey");
        return this.appVarRepository.findAllByAppId(appId, sort).stream().map(l -> BeanUtils.convertType(l, AppVarBoV1.class)).collect(Collectors.toList());
    }

    /**
     * 修改变量
     *
     * @param id    id
     * @param muser 修改者
     * @param value 值
     */
    public void updateAppVarById(Long id, Long muser, String value) {
        AppVariable appVariable = this.appVarRepository.findById(id).orElse(null);
        appVariable.setMtime(LocalDateTime.now());
        appVariable.setMuser(muser);
        appVariable.setVarValue(value);
        this.appVarRepository.saveAndFlush(appVariable);
    }

    /**
     * 删除变量
     *
     * @param id id
     */
    public void delteAppVarById(Long id) {
        appVarRepository.deleteById(id);
    }
}
