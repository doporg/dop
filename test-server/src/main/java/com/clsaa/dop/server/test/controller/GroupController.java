package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.param.CaseGroupParam;
import com.clsaa.dop.server.test.service.create.CaseGroupCreateService;
import com.clsaa.dop.server.test.service.query.CaseGroupQueryService;
import com.clsaa.dop.server.test.service.update.CaseGroupUpdateService;
import com.clsaa.rest.result.Pagination;
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

    @Autowired
    private CaseGroupUpdateService caseGroupUpdateService;

    @Autowired
    private CaseGroupQueryService caseGroupQueryService;

    @PostMapping
    public Long createGroup(@RequestBody CaseGroupParam param) {
        return caseGroupCreateService.create(param).orElse(null);
    }

    @PutMapping
    public Boolean updateGroup(@RequestBody CaseGroupDto caseGroupDto) {
        caseGroupUpdateService.update(caseGroupDto);
        return Boolean.TRUE;
    }

    @GetMapping("/page")
    public Pagination<CaseGroupDto> queryGroup(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return caseGroupQueryService.selectByPage(pageNo, pageSize);
    }
}
