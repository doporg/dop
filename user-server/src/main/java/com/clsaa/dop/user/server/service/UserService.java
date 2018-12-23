package com.clsaa.dop.user.server.service;

import com.clsaa.dop.user.server.config.BizCodes;
import com.clsaa.dop.user.server.dao.UserDao;
import com.clsaa.dop.user.server.enums.UserType;
import com.clsaa.dop.user.server.model.bo.UserBoV1;
import com.clsaa.dop.user.server.model.po.User;
import com.clsaa.dop.user.server.model.vo.UserV1;
import com.clsaa.dop.user.server.util.BeanUtils;
import com.clsaa.dop.user.server.util.TimestampUtil;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户业务实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    /**
     * <p>
     * 添加新用户，如果邮箱已经被注册则会抛出异常，用户默认类型为 {@link UserType#PROGRAMMER}
     * </p>
     *
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public UserBoV1 addUser(String name, String email) {
        User existUser = this.userDao.findUserByEmail(email);
        BizAssert.allowed(existUser == null, BizCodes.REPETITIVE_USER_EMAIL);
        User user = User.builder()
                .id(null)
                .name(name)
                .email(email)
                .ctime(TimestampUtil.now())
                .mtime(TimestampUtil.now())
                .type(UserType.PROGRAMMER)
                .build();
        return BeanUtils.convertType(this.userDao.saveAndFlush(user), UserBoV1.class);
    }

    /**
     * 根据id删除用户
     */
    public boolean deleteUserById(Long id) {
        this.userDao.deleteById(id);
        return true;
    }

    /**
     * 根据id修改用户名称和email
     *
     * @param id    用户id
     * @param name  用户姓名
     * @param email 用户email
     * @return {@link UserBoV1}
     */
    public UserBoV1 updateUser(Long id, String name, String email) {
        //查询出user
        User existUser = this.userDao.findUsersById(id);
        //使用断言判断user是否存在
        BizAssert.found(existUser != null, BizCodes.NOT_FOUND);
        //修改信息
        existUser.setName(name);
        existUser.setEmail(email);
        existUser.setMtime(TimestampUtil.now());
        //保存、更新
        this.userDao.saveAndFlush(existUser);
        return BeanUtils.convertType(existUser, UserBoV1.class);
    }

    /**
     * 根据id查询用户，若不存在返回null
     */
    public UserBoV1 findUserById(Long id) {
        return BeanUtils.convertType(this.userDao.findUsersById(id), UserBoV1.class);
    }

    /**
     * 分页查询用户信息
     */
    public Pagination<UserBoV1> getUserPagination(Integer pageNo, Integer pageSize) {
        int count = (int) this.userDao.count();

        Pagination<UserBoV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);

        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageRequest = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize(), sort);
        List<User> userList = this.userDao.findAll(pageRequest).getContent();
        pagination.setPageList(userList.stream().map(u -> BeanUtils.convertType(u, UserBoV1.class)).collect(Collectors.toList()));
        return pagination;
    }
}
