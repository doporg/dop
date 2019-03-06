package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.service.InterfaceCaseCreateService;
import com.clsaa.dop.server.test.service.ManualCaseCreateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@RestController
public class TestCaseController {

    @Autowired
    private InterfaceCaseCreateService interfaceCaseCreateService;

    @Autowired
    private ManualCaseCreateService manualCaseCreateService;

    @ApiOperation(value = "新增接口测试用例", notes = "创建失败返回null")
    @PostMapping("/interfaceCase")
    public InterfaceCaseDto createCase(@RequestBody InterfaceCaseDto interfaceCase) {
        return interfaceCaseCreateService.create(interfaceCase).orElse(null);
    }

    @ApiOperation(value = "新增手工测试用例", notes = "创建失败返回null")
    @PostMapping("/manualCase")
    public ManualCaseDto createCase(@RequestBody ManualCaseDto manualCase) {
        return manualCaseCreateService.create(manualCase).orElse(null);
    }
}
