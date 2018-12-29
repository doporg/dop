package com.clsaa.dop.server.gateway.controller;

import com.clsaa.dop.server.gateway.config.BizCodes;
import com.clsaa.dop.server.gateway.config.GatewayProperties;
import com.clsaa.dop.server.gateway.model.bo.AccessTokenBoV1;
import com.clsaa.dop.server.gateway.model.bo.ClientBoV1;
import com.clsaa.dop.server.gateway.model.vo.AccessTokenV1;
import com.clsaa.dop.server.gateway.oauth.protocol.GrantType;
import com.clsaa.dop.server.gateway.oauth.security.ClientKeysUtil;
import com.clsaa.dop.server.gateway.oauth.security.FastAes;
import com.clsaa.dop.server.gateway.oauth.security.SignatureVerifier;
import com.clsaa.dop.server.gateway.service.AccessTokenService;
import com.clsaa.dop.server.gateway.service.ClientService;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import com.google.common.io.BaseEncoding;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AccessToken控制器
 *
 * @author 任贵杰
 */
@CrossOrigin
@RestController
public class OauthTokenController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private GatewayProperties properties;

    /**
     * 获取当前服务器时间戳(毫秒)
     *
     * @return {@link long}
     * @author 任贵杰
     * @summary 获取当前服务器时间戳
     * @since 2018-12-29
     */
    @ApiOperation(value = "获取当前服务器时间", notes = "获取当前服务器时间（毫秒）")
    @RequestMapping(value = "/v1/time/epoch", method = RequestMethod.GET)
    public long getEpoch() {
        return System.currentTimeMillis();
    }

    @RequestMapping(value = "/v1/oauth/token", consumes = "application/x-www-form-urlencoded", method = RequestMethod.POST)
    public AccessTokenV1 tokenEndpoint(@RequestParam(SignatureVerifier.PARAM_GRANT_TYPE) String grant_type,
                                       @RequestParam(SignatureVerifier.PARAM_CLIENT_ID) String client_id,
                                       @RequestParam(SignatureVerifier.PARAM_TIMESTAMP) Long timestamp,
                                       @RequestParam(SignatureVerifier.PARAM_NOUCE) String nouce,
                                       @RequestParam(SignatureVerifier.PARAM_SIGNATURE) String signature,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        //检验参数是否放在HTTP URL中，若放在URL中易造成安全问题，拒绝颁发token
        BizAssert.validParam(StringUtils.isEmpty(request.getQueryString()), new BizCode(BizCodes.INVALID_REQUEST.getCode(),
                "请使用x-www-form-urlencoded格式的form表单提交参数，禁止在URL中传递参数"));
        // 客户端密钥禁止传输
        BizAssert.validParam(StringUtils.isEmpty(request.getParameter(SignatureVerifier.PARAM_CLIENT_SECRET)),
                new BizCode(BizCodes.INVALID_REQUEST.getCode(), "禁止传入密匙"));
        // 检查grant_type是否被当前应用支持
        BizAssert.validParam(GrantType.enabled(grant_type), new BizCode(BizCodes.INVALID_GRANT_TYPE.getCode(),
                "当前网关不支持此授权类型"));
        //查询对应client
        ClientBoV1 client = this.clientService.findClientByClientId(client_id);
        BizAssert.validParam(client != null, BizCodes.INVALID_CLIENT);
        //获取明文client_secret
        String clientSecret = ClientKeysUtil.getAesPlainSecret(client.getClientSecret(),
                this.properties.getOauth().getAES().getClientKey());
        //验证签名
        SignatureVerifier.verify(clientSecret,
                request.getRequestURI(),
                request.getMethod(),
                signature,
                timestamp,
                request.getParameterMap());
        //创建AccessToken
        AccessTokenBoV1 accessTokenBoV1 = this.accessTokenService.addAccessToken(client.getId(), client.getAccessTokenValidity());
        //自校验加密
        String returnedAccessToken = FastAes.encrypt(
                BaseEncoding.base64Url().decode(this.properties.getOauth().getAES().getTokenKey()),
                accessTokenBoV1.getToken()).getContent();
        //创建返回的AccessToken信息
        AccessTokenV1 accessTokenV1 = AccessTokenV1.builder()
                .token_type("bearer")
                .expires_in(client.getAccessTokenValidity())
                .access_token(returnedAccessToken)
                .build();
        // 设置响应头,禁止TokenEndpoint缓存响应内容
        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        return accessTokenV1;
    }
}
