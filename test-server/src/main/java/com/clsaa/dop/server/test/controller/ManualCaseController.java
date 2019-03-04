package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.ManualCaseRepository;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xihao
 * @version 1.0
 * @since 04/03/2019
 */
@RestController
public class ManualCaseController {

    @Autowired
    private ManualCaseRepository manualCaseRepository;

    @PostMapping("/manualCase")
    public ManualCase createCase(@RequestBody ManualCase manualCase) {
        return manualCaseRepository.save(manualCase);
    }
}
