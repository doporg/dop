package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joyren
 */
public interface UserRepository extends JpaRepository<User, Long> {

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

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return {@link User}
     */
    User findUserByName(String name);
}
