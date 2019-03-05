package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.dao.CaseResultRepository;
import com.clsaa.dop.server.test.dao.InterfaceRepository;
import com.clsaa.dop.server.test.dao.ManualCaseRepository;
import com.clsaa.dop.server.test.dao.TestStepRepository;
import com.clsaa.dop.server.test.model.po.CaseResult;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.po.TestStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CaseResultRepository caseResultRepository;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private ManualCaseRepository manualCaseRepository;

    @Autowired
    private TestStepRepository testStepRepository;

    @PostMapping("/interfaceCase")
    public InterfaceCase createCase(@RequestBody InterfaceCase interfaceCase) {
        return interfaceRepository.save(interfaceCase);
    }

    @PostMapping("/caseResult")
    public CaseResult createCaseResult(@RequestBody CaseResult caseResult) {
        return caseResultRepository.save(caseResult);
    }

    @PostMapping("/testStep")
    public TestStep createTestStep(@RequestBody TestStep testStep) {
        return testStepRepository.save(testStep);
    }
}
