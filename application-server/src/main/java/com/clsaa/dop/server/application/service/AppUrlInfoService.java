package com.clsaa.dop.server.application.service;


import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.AppUrlInfoRepository;
import com.clsaa.dop.server.application.model.bo.AppUrlInfoBoV1;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "AppUrlInfoService")
public class AppUrlInfoService {
    @Autowired
    private AppUrlInfoRepository appUrlInfoRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermissionConfig permissionConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据appId查询
     *
     * @param appId appId
     * @return {@link AppUrlInfoBoV1}
     */
    public AppUrlInfoBoV1 findAppUrlInfoByAppId(Long appId) {

        logger.info("[findAppUrlInfoByAppId] Request coming: appId={}",appId);
        return BeanUtils.convertType(appUrlInfoRepository.findByAppId(appId), AppUrlInfoBoV1.class);
    }

    public void updateAppUrlInfoByAppId(Long id, Long loginUser, String warehouseUrl, String imageUrl, String productionDbUrl, String testDbUrl, String productionDomain, String testDomain) {
//        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditAppUrl(), loginUser)
//                , BizCodes.NO_PERMISSION);
        logger.info("[updateAppUrlInfoByAppId] Request coming: id={}, loginUser={}, warehouseUrl={}, imageUrl={}, productionDbUrl={}, testDbUrl={}, productionDomain={}, testDomain={}",id,loginUser,warehouseUrl,imageUrl,productionDbUrl,testDbUrl,productionDomain,testDomain);
        AppUrlInfo appUrlInfo = appUrlInfoRepository.findByAppId(id);
        appUrlInfo.setWarehouseUrl(warehouseUrl);
        appUrlInfo.setImageUrl(imageUrl);
        appUrlInfo.setProductionDbUrl(productionDbUrl);
        appUrlInfo.setTestDbUrl(testDbUrl);
        appUrlInfo.setProductionDomain(productionDomain);
        appUrlInfo.setTestDomain(testDomain);
        appUrlInfo.setMtime(LocalDateTime.now());
        appUrlInfo.setMuser(loginUser);
        appUrlInfoRepository.saveAndFlush(appUrlInfo);
    }

    /**
     * 删除应用
     *
     * @param id 项目Id
     */
    public void deleteAppUrlInfo(Long id) {
        //Long id = Long.parseLong(sId);
        logger.info("[deleteAppUrlInfo] Request coming: id={}",id);
        this.appUrlInfoRepository.deleteById(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }

    /**
     * 创建应用
     *
     * @param appUrlInfo 应用Url信息
     */
    public void createAppUrlInfo(AppUrlInfo appUrlInfo) {
        //Long id = Long.parseLong(sId);
        logger.info("[createAppUrlInfo] Request coming: appUrlInfo");
        this.appUrlInfoRepository.saveAndFlush(appUrlInfo);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }


}
