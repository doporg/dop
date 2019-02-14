package com.clsaa.dop.server.message.controller;

import com.clsaa.dop.server.message.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件发送接口实现类
 * 邮件发送的REST API接口将在第三个迭代改为消息队列接收的方式
 *
 * @author joyren
 * @deprecated
 */
@RestController
@CrossOrigin
public class EmailController {

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "发送邮件", notes = "发送邮件，邮件内容由业务方自定义")
    @PostMapping("/v1/email")
    public void addEmail(@ApiParam(value = "发送人", required = true) @RequestParam("from") String from,
                         @ApiParam(value = "接收人", required = true) @RequestParam("to") String to,
                         @ApiParam(value = "主题", required = true) @RequestParam("subject") String subject,
                         @ApiParam(value = "内容", required = true) @RequestParam("text") String text) {
        this.emailService.addEmail(from, to, subject, text);
    }
}
