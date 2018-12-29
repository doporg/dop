package com.clsaa.dop.server.gateway.zuul.filter.pre;

import com.clsaa.dop.server.gateway.config.BizCodes;
import com.clsaa.dop.server.gateway.config.GatewayProperties;
import com.clsaa.dop.server.gateway.model.bo.AccessTokenBoV1;
import com.clsaa.dop.server.gateway.oauth.security.CryptoResult;
import com.clsaa.dop.server.gateway.oauth.security.FastAes;
import com.clsaa.dop.server.gateway.service.AccessTokenService;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import com.google.common.io.BaseEncoding;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 验证AccessToken的过滤器
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 验证AccessToken
 * @since 2018-12-29
 */
public class AccessTokenZuulFilter extends ZuulFilter {
    /**
     * http 授权头
     */
    private static final String HTTP_AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private GatewayProperties properties;
    @Autowired
    private AccessTokenService accessTokenService;
    private byte[] tokenSecret = null;

    @PostConstruct
    void init() {
        this.tokenSecret = BaseEncoding.base64Url()
                .decode(this.properties.getOauth().getAES().getTokenKey());
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        System.out.println("should filter");
        if (ctx.getRequest().getRequestURI().contains("swagger")
        ||ctx.getRequest().getRequestURI().contains("api-docs")) {
            System.out.println("swagger 请求放行");
            return false;
        }
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * order越小优先级越高
     */
    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * 注意：AccessToken优先级最高，如果可能的话，只使用RequestHeader来进行业务判断。
     * </p>
     * 没必要为了非法请求浪费内存，耗费CPU去解析请求参数（request.getParameter会触发解析请求参数)
     *
     * @see com.netflix.zuul.IZuulFilter#run()
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 检查是否有header
        String bearToken = ctx.getRequest().getHeader(HTTP_AUTHORIZATION_HEADER);
        BizAssert.validParam(StringUtils.hasText(bearToken), new BizCode(BizCodes.INVALID_REQUEST.getCode(), "Empty Authorization Header"));
        // bearer和token之间用一个空格分隔
        // 参考 http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html#authz-header
        String[] tokens = StringUtils.split(bearToken, " ");
        BizAssert.validParam(tokens != null && tokens.length == 2, new BizCode(BizCodes.INVALID_REQUEST.getCode(),
                "invalid authorization bearer token"));
        // token自校验解密
        CryptoResult cryptoResult = FastAes.decrypt(this.tokenSecret, tokens[1]);
        // 是否通过自校验
        BizAssert.validParam(cryptoResult.isOK(), BizCodes.INVALID_ACCESS_TOKEN);
        // 查找token
        AccessTokenBoV1 token = this.accessTokenService.findAccessTokenByToken(cryptoResult.getContent());
        // 是否失效
        BizAssert.authorized(token != null && !token.isExpired(), BizCodes.ACCESS_TOKEN_EXPIRED);
        return null;
    }
}
