package com.panghu.seat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panghu.seat.domain.Seat;
import com.panghu.seat.mapper.SeatMapper;
import com.panghu.seat.service.ISeatService;
import org.springframework.stereotype.Service;

@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements ISeatService {

}
