package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName game_plan
 */

@Data
@TableName(value = "game_plan")
@Schema(description = "游戏计划实体类")
public class GamePlan implements Serializable {


    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String id;

    /**
     * 游戏名称
     */
    @NotNull(message = "游戏名称不能为空")
    @Schema(description = "游戏名称")
    private String name;

    /**
     * 开始游玩日期
     */
    @Schema(description = "开始游玩日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date playStartDate;

    /**
     * 通关日期
     */
    @Schema(description = "通关日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfClearance;

    /**
     * 全成就/白金日期
     */
    @Schema(description = "全成就/白金日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date platinumDate;

    /**
     * 游戏平台
     */
    @NotNull(message = "游戏平台不能为空")
    @Schema(description = "游戏平台")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer gamesPlatform;

    /**
     * 是否计划全成就/白金
     */
    @NotNull(message = "是否计划全成就/白金")
    @Schema(description = "是否计划全成就/白金")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isPlanPlatinum;

    /**
     * 是否全成就/白金
     */
    @Schema(description = "是否全成就/白金")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer isPlatinum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}