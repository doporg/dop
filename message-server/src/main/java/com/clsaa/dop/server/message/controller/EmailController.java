package com.clsaa.dop.server.message.controller;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.message.model.dto.EmailDtoV1;
import com.clsaa.dop.server.message.model.po.Email;
import com.clsaa.dop.server.message.mq.MessageQueueException;
import com.clsaa.dop.server.message.mq.MessageSender;
import com.clsaa.dop.server.message.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @Autowired
    private MessageSender messageSender;

    @ApiOperation(value = "发送邮件", notes = "发送邮件，邮件内容由业务方自定义")
    @PostMapping("/v1/email")
    public void addEmailV1(@ApiParam(value = "发送人", required = true) @RequestParam("from") String from,
                           @ApiParam(value = "接收人", required = true) @RequestParam("to") String to,
                           @ApiParam(value = "主题", required = true) @RequestParam("subject") String subject,
                           @ApiParam(value = "内容", required = true) @RequestParam("text") String text) {
        this.emailService.addEmail(from, to, subject, text);
    }

    @Value("${message.mq.RocketMQ.emailTopic}")
    private String topic;

    @ApiOperation(value = "发送邮件", notes = "发送邮件，邮件内容由业务方自定义")
    @PostMapping("/v2/email")
    public void addEmailV2(@ApiParam(value = "发送人", required = true) @RequestParam("from") String from,
                           @ApiParam(value = "接收人", required = true) @RequestParam("to") String to,
                           @ApiParam(value = "主题", required = true) @RequestParam("subject") String subject,
                           @ApiParam(value = "内容", required = true) @RequestParam("text") String text) {
        EmailDtoV1 email = new EmailDtoV1(from, to, subject, text);
        try {
            this.messageSender.send(topic, UUID.randomUUID().toString(), JSON.toJSONString(email));
        } catch (MessageQueueException e) {
            e.printStackTrace();
        }
    }
}
