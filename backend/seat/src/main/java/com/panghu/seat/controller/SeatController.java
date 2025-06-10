package com.panghu.seat.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.seat.common.Constants;
import com.panghu.seat.common.Result;
import com.panghu.seat.common.SeatStatus;
import com.panghu.seat.domain.Seat;
import com.panghu.seat.domain.User;
import com.panghu.seat.service.ISeatService;
import com.panghu.seat.service.IUserService;
import com.panghu.seat.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @Resource
    private ISeatService seatService;
    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    @PostMapping
    public Result save(@RequestBody Seat seat) {
        if (seat.getId() == null) {
            seat.setState(SeatStatus.AVAILABLE);
        }
        seatService.saveOrUpdate(seat);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        seatService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        seatService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(seatService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(seatService.getById(id));
    }

    @GetMapping("/search")
    public Result search(@RequestParam(required = false) String name,
                        @RequestParam(required = false) String status,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Seat::getName, name);
        }
        if (status != null && !status.isEmpty()) {
            try {
                SeatStatus seatStatus = SeatStatus.valueOf(status);
                wrapper.eq(Seat::getState, seatStatus);
            } catch (IllegalArgumentException e) {
                return Result.error(Constants.CODE_400, "无效的座位状态");
            }
        }
        wrapper.orderByDesc(Seat::getId);
        return Result.success(seatService.page(new Page<>(pageNum, pageSize), wrapper));
    }

    @PostMapping("/reserve/{id}")
    public Result reserve(@PathVariable Integer id, 
                         @RequestParam String startTime,
                         @RequestParam String endTime) {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return Result.error(Constants.CODE_600, "座位不存在");
        }
        if (seat.getState() != SeatStatus.AVAILABLE) {
            return Result.error(Constants.CODE_600, "座位当前不可预约");
        }
        
        seat.setUserId(TokenUtils.getCurrentUser().getId());
        seat.setUserName(TokenUtils.getCurrentUser().getNickname());
        seat.setState(SeatStatus.RESERVED);
        seat.setStartTime(startTime);
        seat.setEndTime(endTime);
        seatService.updateById(seat);
        return Result.success();
    }

    @PostMapping("/start-use/{id}")
    public Result startUse(@PathVariable Integer id) {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return Result.error(Constants.CODE_600, "座位不存在");
        }
        if (seat.getState() != SeatStatus.RESERVED || 
            !seat.getUserId().equals(TokenUtils.getCurrentUser().getId())) {
            return Result.error(Constants.CODE_600, "座位状态不允许使用或不是您的预约");
        }
        
        seat.setState(SeatStatus.IN_USE);
        seatService.updateById(seat);
        return Result.success();
    }

    @PostMapping("/end-use/{id}")
    public Result endUse(@PathVariable Integer id) {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return Result.error(Constants.CODE_600, "座位不存在");
        }
        if (seat.getState() != SeatStatus.IN_USE || 
            !seat.getUserId().equals(TokenUtils.getCurrentUser().getId())) {
            return Result.error(Constants.CODE_600, "座位状态不允许结束使用或不是您的使用");
        }
        
        seat.setState(SeatStatus.AVAILABLE);
        seat.setUserId(null);
        seat.setUserName(null);
        seat.setStartTime(null);
        seat.setEndTime(null);
        seatService.updateById(seat);
        return Result.success();
    }

    @PostMapping("/maintenance/{id}")
    public Result setMaintenance(@PathVariable Integer id) {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return Result.error(Constants.CODE_600, "座位不存在");
        }
        seat.setState(SeatStatus.MAINTENANCE);
        seatService.updateById(seat);
        return Result.success();
    }

    @PostMapping("/available/{id}")
    public Result setAvailable(@PathVariable Integer id) {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return Result.error(Constants.CODE_600, "座位不存在");
        }
        seat.setState(SeatStatus.AVAILABLE);
        seatService.updateById(seat);
        return Result.success();
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                          @RequestParam Integer pageNum,
                          @RequestParam Integer pageSize) {
        QueryWrapper<Seat> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(seatService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Seat> list = seatService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Seat信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Seat> list = reader.readAll(Seat.class);

        seatService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

