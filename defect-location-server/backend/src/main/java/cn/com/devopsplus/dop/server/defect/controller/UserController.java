package cn.com.devopsplus.dop.server.defect.controller;

import cn.com.devopsplus.dop.server.defect.config.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/id")
    public Long getUserID(@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserID) {
        return loginUserID;
    }
}
