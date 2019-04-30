package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.service.ImageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;

/**
 * <p>
 * 镜像API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-12
 */


@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class ImageController {
    @Autowired
    private ImageService imageService;

    @ApiOperation(value = "查询代码仓库地址", notes = "查询代码仓库地址")
    @GetMapping(value = "/image_url_list")
    public List<String> getImageUrlList(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                        @ApiParam(value = "projectName", name = "projectName", required = true) @RequestParam(value = "projectName") String projectName) {
        return this.imageService.getImageUrls(projectName, loginUser);
    }
}
