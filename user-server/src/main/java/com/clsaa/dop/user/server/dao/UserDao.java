package com.clsaa.dop.user.server.dao;


import com.clsaa.dop.user.server.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joyren
 */
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return {@link User}
     */
    User findUsersById(Long id);

    /**
     * 根据email查询用户
     *
     * @param email 用户email
     * @return {@link User}
     */
    User findUserByEmail(String email);
}
