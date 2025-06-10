package com.panghu.seat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panghu.seat.controller.dto.UserDTO;
import com.panghu.seat.controller.dto.UserPasswordDTO;
import com.panghu.seat.domain.User;

public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);
}
