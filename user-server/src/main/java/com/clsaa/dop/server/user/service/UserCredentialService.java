package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.dao.UserCredentialRepository;
import com.clsaa.dop.server.user.model.po.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户凭据业务实现
 *
 * @author joyren
 */
@Service
public class UserCredentialService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    /**
     * 添加用户凭据
     *
     * @param userId     用户id
     * @param identifier 凭据标识
     * @param credential 凭据
     * @param type       凭据类型
     */
    public void addUserCredential(Long userId, String identifier, String credential, UserCredential.Type type) {
        UserCredential userCredential = UserCredential.builder()
                .userId(userId)
                .identifier(identifier)
                .credential(credential)
                .type(type)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .build();
        this.userCredentialRepository.saveAndFlush(userCredential);
    }

    /**
     * 根据类型和用户id更新用户凭据
     *
     * @param userId     用户id
     * @param credential 凭据
     */
    public void updateUserCredentialByUserIdAndType(Long userId, String credential, UserCredential.Type type) {
        UserCredential userCredential = this.userCredentialRepository
                .findUserCredentialByUserIdAndType(userId, UserCredential.Type.DOP_LOGIN_EMAIL);
        userCredential.setCredential(credential);
        userCredential.setMtime(LocalDateTime.now());
        this.userCredentialRepository.saveAndFlush(userCredential);
    }
}
