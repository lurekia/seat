package com.panghu.seat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panghu.seat.domain.User;
import com.panghu.seat.controller.dto.UserPasswordDTO;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    @Update("update user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
}
