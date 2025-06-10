package com.panghu.seat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panghu.seat.domain.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
