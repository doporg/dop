package com.clsaa.dop.server.application.service;


import com.clsaa.dop.server.application.dao.AppUrlInfoRepository;
import com.clsaa.dop.server.application.model.bo.AppUrlInfoBoV1;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "AppUrlInfoService")
public class AppUrlInfoService {
    @Autowired
    private AppUrlInfoRepository appUrlInfoRepository;

    /**
     * 通过projectId分页查询应用
     *
     * @param appId appId
     * @return {@link AppUrlInfoBoV1}
     */
    public AppUrlInfoBoV1 findAppUrlInfoByAppId(Long appId) {
        return BeanUtils.convertType(appUrlInfoRepository.findByAppId(appId), AppUrlInfoBoV1.class);
    }

    public void updateAppUrlInfoByAppId(Long id, Long muser, String warehouseUrl, String productionDbUrl, String testDbUrl, String productionDomain, String testDomain) {
        AppUrlInfo appUrlInfo = appUrlInfoRepository.findByAppId(id);
        appUrlInfo.setWarehouseUrl(warehouseUrl);
        appUrlInfo.setProductionDbUrl(productionDbUrl);
        appUrlInfo.setTestDbUrl(testDbUrl);
        appUrlInfo.setProductionDomain(productionDomain);
        appUrlInfo.setTestDomain(testDomain);
        appUrlInfo.setMtime(LocalDateTime.now());
        appUrlInfo.setMuser(muser);
        appUrlInfoRepository.saveAndFlush(appUrlInfo);
    }

    /**
     * 删除应用
     *
     * @param id 项目Id
     */
    public void deleteAppUrlInfo(Long id) {
        //Long id = Long.parseLong(sId);
        this.appUrlInfoRepository.deleteById(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }

    /**
     * 删除应用
     *
     * @param appUrlInfo 应用Url信息
     */
    public void createAppUrlInfo(AppUrlInfo appUrlInfo) {
        //Long id = Long.parseLong(sId);
        this.appUrlInfoRepository.saveAndFlush(appUrlInfo);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }


}
