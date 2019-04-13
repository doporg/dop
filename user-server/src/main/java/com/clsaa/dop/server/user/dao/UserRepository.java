package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.User;
import com.sun.tools.javac.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 根据关键字查询用户
     *
     * @param key 关键字（邮箱或密码前缀）
     * @return {@link List<User>}
     */
    @Query(value = "select u from User u where u.email like :key% or u.name like  :key%")
    List<User> searchUserByEmailOrPassword(@Param("key") String key);
}
