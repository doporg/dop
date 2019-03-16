package com.clsaa.dop.server.code.dao;

import com.clsaa.dop.server.code.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wsy
 */

@Mapper
public interface UserMapper {

    public String findUserAccessToken(@Param("username") String username);

}
