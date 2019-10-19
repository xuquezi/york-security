package com.example.york.dao;

import com.example.york.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 通过用户名查找用户 用户名需保持唯一
     * @param username
     * @return
     */
    List<User> findByUsername(@Param("username") String username);

    /**
     * 分页查询用户数据
     * @param username
     * @param pageSize
     * @param start
     * @return
     */
    List<User> selectUserList(@Param("username") String username, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    /**
     * 查询总记录数
     * @param username
     * @return
     */
    Integer countUserList(@Param("username")String username);

    Integer updateUser(User user);

    Integer stopUser(@Param("userId")Integer userId,@Param("status") Integer status);

    Integer useUser(@Param("userId")Integer userId,@Param("status")Integer status);

    Integer deleteUser(@Param("userId")Integer userId);

}
