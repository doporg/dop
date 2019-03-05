package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.service.InterfaceCaseCreateService;
import com.clsaa.dop.server.test.service.ManualCaseCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private InterfaceCaseCreateService interfaceCaseCreateService;

    @Autowired
    private ManualCaseCreateService manualCaseCreateService;

    @PostMapping("/interfaceCase")
    public InterfaceCaseDto createCase(@RequestBody InterfaceCaseDto interfaceCase) {
        return interfaceCaseCreateService.create(interfaceCase);
    }

    @PostMapping("/manualCase")
    public ManualCaseDto createCase(@RequestBody ManualCaseDto manualCase) {
        return manualCaseCreateService.create(manualCase);
    }
}
