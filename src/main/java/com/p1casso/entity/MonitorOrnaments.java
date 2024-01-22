package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 待监听的饰品
 *
 * @TableName monitor_ornaments
 */
@TableName(value = "monitor_ornaments")
@Data
public class MonitorOrnaments implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 饰品id
     */
    private String ornamentsId;

    public MonitorOrnaments(String ornamentsId) {
        this.ornamentsId = ornamentsId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}