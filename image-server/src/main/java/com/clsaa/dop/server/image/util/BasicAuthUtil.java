package com.clsaa.dop.server.image.util;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;

/**
 * <p>
 *    Basic Auth工具类
 * </p>
 * @author xzt
 * @since 2019-3-28
 */
public class BasicAuthUtil {
    /**
     * 通过username和password产生basic auth
     * @param username 用户名
     * @param password 用户密码
     * @return {@link String} basic auth
     */
    @Autowired
    private static UserFeign userFeign;

    public static String createAuth(Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String userNamePassword = userCredentialDto.getIdentifier() +":"+userCredentialDto.getCredential();
        String auth = "Basic "+Base64.getEncoder().encodeToString(userNamePassword.getBytes());
        return auth;
    }
}
