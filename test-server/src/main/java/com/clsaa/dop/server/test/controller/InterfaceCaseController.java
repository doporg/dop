package com.clsaa.dop.server.test.controller;

import com.alibaba.fastjson.JSONArray;
import com.clsaa.dop.server.test.config.BizCodes;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceExecuteLogDto;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import com.clsaa.dop.server.test.model.param.InterfaceStageParam;
import com.clsaa.dop.server.test.service.*;
import com.clsaa.dop.server.test.util.ValidateUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.clsaa.dop.server.test.doExecute.TestManager.execute;
import static com.clsaa.dop.server.test.util.ValidateUtils.validate;
import static java.util.Objects.requireNonNull;

/**
 * 接口测试用例相关api
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@RestController
@CrossOrigin
@RequestMapping("/interfaceCases")
public class InterfaceCaseController {

    @Autowired
    private InterfaceCaseCreateService interfaceCaseCreateService;

    @Autowired
    private InterfaceCaseQueryService interfaceCaseQueryService;

    @Autowired
    private InterfaceStageCreateService interfaceStageCreateService;

    @Autowired
    private InterfaceCaseLogQueryService logQueryService;

    @Autowired
    private CaseParamCreateService caseParamCreateService;

    @ApiOperation(value = "新增接口测试用例", notes = "创建失败返回null")
    @PostMapping
    public Long createCase(@RequestBody @Valid InterfaceCaseParam interfaceCase) {
        return interfaceCaseCreateService.create(interfaceCase).orElse(null);
    }

    @ApiOperation(value = "新增接口测试用例测试脚本", notes = "创建失败500，message为错误信息")
    @PostMapping("/stages")
    public List<Long> createStages(@RequestBody JSONArray jsonArray) {
        List<InterfaceStageParam> stageParams = jsonArray.toJavaList(InterfaceStageParam.class);
        validate(stageParams);
        return interfaceStageCreateService.create(stageParams);
    }

    @ApiOperation(value = "新增接口测试用例全局参数", notes = "创建失败500，message为错误信息")
    @PostMapping("/caseParams")
    public List<Long> createParams(@RequestBody JSONArray jsonArray) {
        List<CaseParamRef> caseParamRefs = jsonArray.toJavaList(CaseParamRef.class);
        validate(caseParamRefs);
        return caseParamCreateService.create(caseParamRefs);
    }


    @ApiOperation(value = "根据id查询接口测试用例信息", notes = "参数为需要获取的接口测试用例ID")
    @GetMapping("/{id}")
    public InterfaceCaseDto getCase(@PathVariable("id") Long id) {
        return interfaceCaseQueryService.selectByIds(id).orElse(null);
    }

    @ApiOperation(value = "根据id执行脚本测试用例", notes = "参数为需要执行的接口测试用例ID")
    @GetMapping("/execute/{id}")
    public InterfaceCaseDto getExecuteResult(@PathVariable("id") Long id) {
        BizAssert.validParam(id >= 0, BizCodes.INVALID_PARAM.getCode(), "请求执行的测试用例id不合法");
        InterfaceCaseDto interfaceCaseDto = interfaceCaseQueryService.selectByIds(id).orElse(null);
        return execute(requireNonNull(interfaceCaseDto));
    }

    @ApiOperation(value = "分页获取接口测试用例数据")
    @GetMapping("/page")
    public Pagination<InterfaceCaseDto> queryCase(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return interfaceCaseQueryService.selectByPage(pageNo, pageSize);
    }

    @ApiOperation(value = "分页获取接口测试用例执行日志")
    @GetMapping("/logs/page/{caseId}")
    public Pagination<InterfaceExecuteLogDto> queryLogs(@PathVariable("caseId") Long caseId,
                                                        @RequestParam("pageNo") int pageNo,
                                                        @RequestParam("pageSize") int pageSize) {
        return logQueryService.getExecuteLogs(caseId, pageNo, pageSize);
    }


}
