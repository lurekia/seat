package com.panghu.seat.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.panghu.seat.common.SeatStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 座位管理
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@ApiModel(value = "Seat对象", description = "座位管理")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Id")
    @Alias("Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    @Alias("名称")
    private String name;

    @ApiModelProperty("简介")
    @Alias("简介")
    private String info;

    @ApiModelProperty("座位照片")
    @Alias("座位照片")
    private String img;

    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer userId;

    @ApiModelProperty("用户名称")
    @Alias("用户名称")
    private String userName;

    @ApiModelProperty("座位状态")
    @Alias("座位状态")
    private SeatStatus state;

    @ApiModelProperty("开始使用时间")
    @Alias("开始使用时间")
    private String startTime;

    @ApiModelProperty("结束使用时间")
    @Alias("结束使用时间")
    private String endTime;

}
