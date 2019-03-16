package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.bo.NameSpaceBO;
import com.clsaa.dop.server.image.model.vo.NameSpaceVO;
import com.clsaa.dop.server.image.service.NameSpaceService;
import com.clsaa.dop.server.image.util.BeanUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  用户命名空间API接口实现类
 *  @author xzt
 *  @since 2019-3-7
 */
@RestController
@CrossOrigin
@Api(value = "NameSpaceController|一个用来处理NameSpace请求的控制器")
public class NameSpaceController {
    @Autowired
    private NameSpaceService nameSpaceService;

    @ApiOperation(value = "根据命名空间名和创建人获取命名空间信息",notes = "根据命名空间的创建人和命名空间的name查询命名空间信息" +
            "不存在返回null")
    @GetMapping("/namespace/{name}")
    public NameSpaceVO getNameSpace(@PathVariable("name")String name, @RequestParam("ouser")Long ouser){
        return BeanUtils.convertType(nameSpaceService.getNameSpace(ouser,name),NameSpaceVO.class);
    }

    @ApiOperation(value = "/namespaces")
    public List<NameSpaceVO> getNameSpaces(@RequestParam("ouser")Long ouser){
        List<NameSpaceBO> nameSpaceBOS = nameSpaceService.getNameSpaces(ouser);
        List<NameSpaceVO> nameSpaceVOS = new ArrayList<>();
        for (Iterator i = nameSpaceBOS.iterator();i.hasNext();){

        }
        return nameSpaceService.getNameSpaceList(ouser);
    }

}
