package com.p1casso.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @TableName game_plan
 */
@TableName(value = "game_plan")
@Data
public class GamePlan implements Serializable {


    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String id;

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 开始游玩日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date playStartDate;

    /**
     * 通关日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfClearance;

    /**
     * 全成就/白金日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date platinumDate;

    /**
     * 游戏平台
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer gamesPlatform;

    /**
     * 是否打算全成就/白金
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isPlanPlatinum;

    /**
     * 是否全成就/白金
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isPlatinum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}