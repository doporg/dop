package com.clsaa.dop.user.server.controller;

import com.clsaa.dop.user.server.model.vo.UserV1;
import com.clsaa.dop.user.server.service.UserService;
import com.clsaa.dop.user.server.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户API接口实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * <p>
     * 添加用户，若用户email地址已注册则不允许注册
     * </p>
     *
     * @param name  用户姓名
     * @param email 用户email地址
     * @return {@link UserV1}
     * @summary 添加用户
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @ApiOperation(value = "添加用户", notes = "添加一个用户，若用户email地址已注册则不允许注册")
    @PostMapping("/v1/user")
    public UserV1 addUserV1(@ApiParam(value = "用户姓名", required = true) @RequestParam("name") String name,
                            @ApiParam(value = "用户email地址", required = true) @RequestParam("email") String email) {
        return BeanUtils.convertType(this.userService.addUser(name, email), UserV1.class);
    }

    /**
     * <p>
     * 删除用户，直接物理删除
     * </p>
     *
     * @param id 用户id
     * @return {@link UserV1}
     * @summary 删除用户
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @ApiOperation(value = "删除用户", notes = "删除一个用户，物理删除")
    @DeleteMapping("/v1/user/{id}")
    public boolean deleteUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id) {
        return this.userService.deleteUserById(id);
    }

    /**
     * <p>
     * 根据id更新用户信息，只允许修改email和姓名
     * </p>
     *
     * @param id    用户id
     * @param name  用户姓名
     * @param email 用户email地址
     * @return {@link UserV1}
     * @summary 更新用户信息
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @ApiOperation(value = "修改用户信息", notes = "根据id更新用户信息，只允许修改email和姓名")
    @PutMapping("/v1/user/{id}")
    public UserV1 updateUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id,
                                   @ApiParam(value = "用户姓名", required = true) @RequestParam("name") String name,
                                   @ApiParam(value = "用户email地址", required = true) @RequestParam("email") String email) {
        return BeanUtils.convertType(this.userService.updateUser(id, name, email), UserV1.class);
    }

    /**
     * <p>
     * 根据id查询用户信息，若用户不存在返回null
     * </p>
     *
     * @param id 用户id
     * @return {@link UserV1}
     * @summary 查询用户信息
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @ApiOperation(value = "查询用户信息", notes = "根据id查询用户信息，若用户不存在返回null")
    @GetMapping("/v1/user/{id}")
    public UserV1 findUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id) {
        return BeanUtils.convertType(this.userService.findUserById(id), UserV1.class);
    }

    /**
     * <p>
     * 分页查询用户信息
     * </p>
     *
     * @param pageNo   页号，默认为1
     * @param pageSize 页大小，默认为10
     * @return {@link Pagination <UserV1>}
     * @summary 分页查询用户信息
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @GetMapping(value = "/v1/user/pagination")
    public Pagination<UserV1> getUserPaginationV1(@ApiParam(value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                  @ApiParam(value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pagination result = BeanUtils.convertType(this.userService.getUserPagination(pageNo, pageSize), Pagination.class);
        return result;
    }
}
