package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.param.CaseGroupParam;
import com.clsaa.dop.server.test.service.create.CaseGroupCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试分组相关接口
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private CaseGroupCreateService caseGroupCreateService;

    @PostMapping
    public Long createGroup(@RequestBody CaseGroupParam param) {
        return caseGroupCreateService.create(param).orElse(null);
    }

}
