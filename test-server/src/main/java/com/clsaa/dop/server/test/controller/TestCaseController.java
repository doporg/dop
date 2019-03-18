package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.mapper.InterfaceCaseParamMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import com.clsaa.dop.server.test.service.InterfaceCaseCreateService;
import com.clsaa.dop.server.test.service.InterfaceCaseQueryService;
import com.clsaa.dop.server.test.service.ManualCaseCreateService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Autowired
    private InterfaceCaseQueryService interfaceCaseQueryService;

    @Autowired
    private InterfaceCaseParamMapper interfaceCaseParamMapper;

    @ApiOperation(value = "新增接口测试用例", notes = "创建失败返回null")
    @PostMapping("/interfaceCase")
    public InterfaceCaseDto createCase(@RequestBody @Valid InterfaceCaseParam interfaceCase) {
        InterfaceCaseDto toCreate = interfaceCaseParamMapper.convert(interfaceCase).orElse(null);
        return interfaceCaseCreateService.create(toCreate).orElse(null);
    }

    @ApiOperation(value = "新增手工测试用例", notes = "创建失败返回null")
    @PostMapping("/manualCase")
    public ManualCaseDto createCase(@RequestBody ManualCaseDto manualCase) {
        return manualCaseCreateService.create(manualCase).orElse(null);
    }

    @ApiOperation(value = "根据id查询接口测试用例信息")
    @GetMapping("/interfaceCases/{id}")
    public InterfaceCaseDto getCase(@PathVariable("id")Long id) {
        return interfaceCaseQueryService.selectByPk(id).get();
    }

    @GetMapping("/interfaceCases/execute/{id}")
    public String getExecuteResult(@PathVariable("id") Long id) {
        // todo 实现执行逻辑
        return "Success!";
    }
}
