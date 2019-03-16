package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.dao.NamespaceRepository;
import com.clsaa.dop.server.image.model.bo.NameSpaceBO;
import com.clsaa.dop.server.image.model.po.NameSpacePO;
import com.clsaa.dop.server.image.util.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     命名空间服务实现
 * </p>
 * @author xzt
 * @since 2019-3-8
 */
@Service
public class NameSpaceService {
    @Autowired
    private NamespaceRepository namespaceRepository;
    private static final Log log = LogFactory.getLog(NameSpaceService.class);

    public List<NameSpaceBO> getNameSpaces(Long ouser) {
        List<NameSpaceBO> nameSpaceBOs = new ArrayList<>();
        List<NameSpacePO> nameSpacePOs = namespaceRepository.findAllByOuser(ouser);
        int numOfNameSpace = nameSpacePOs.size();
        if (numOfNameSpace != 0){
            for (int i=0;i<numOfNameSpace;i++){
                NameSpaceBO nameSpaceBO = BeanUtils.convertType(nameSpacePOs.get(i),NameSpaceBO.class);
                nameSpaceBOs.add(nameSpaceBO);
            }
            return nameSpaceBOs;
        }
        return null;
    }

    public NameSpaceBO getNameSpace(Long ouser, String name) {

        NameSpacePO nameSpacePO = namespaceRepository.findByNameAndOuser(name,ouser);
        NameSpaceBO nameSpaceBO = BeanUtils.convertType(nameSpacePO,NameSpaceBO.class);
        nameSpaceBO.setRepoNum((long)nameSpacePO.getImageRepoPOS().size());
        return nameSpaceBO;
    }
}
