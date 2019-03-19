package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import com.clsaa.dop.server.test.model.param.InterfaceStageParam;
import com.clsaa.dop.server.test.service.InterfaceCaseCreateService;
import com.clsaa.dop.server.test.service.InterfaceCaseQueryService;
import com.clsaa.dop.server.test.service.InterfaceStageCreateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@RestController
@RequestMapping("/interfaceCases")
public class InterfaceCaseController {

    @Autowired
    private InterfaceCaseCreateService interfaceCaseCreateService;

    @Autowired
    private InterfaceCaseQueryService interfaceCaseQueryService;

    @Autowired
    private InterfaceStageCreateService interfaceStageCreateService;

    @ApiOperation(value = "新增接口测试用例", notes = "创建失败返回null")
    @PostMapping
    public InterfaceCaseParam createCase(@RequestBody @Valid InterfaceCaseParam interfaceCase) {
        return interfaceCaseCreateService.create(interfaceCase).orElse(null);
    }

    @ApiOperation(value = "新增接口测试用例测试脚本", notes = "创建失败500，message为错误信息")
    @PostMapping("/stages")
    public List<InterfaceStageParam> createStages(@RequestBody @Valid List<InterfaceStageParam> stageParams) {
        return interfaceStageCreateService.create(stageParams);
    }

    @ApiOperation(value = "根据id查询接口测试用例信息")
    @GetMapping("/{id}")
    public InterfaceCaseDto getCase(@PathVariable("id") Long id) {
        return interfaceCaseQueryService.selectByPk(id).orElse(null);
    }

    @GetMapping("/execute/{id}")
    public String getExecuteResult(@PathVariable("id") Long id) {
        // todo 实现执行逻辑
        return "Success!";
    }
}
