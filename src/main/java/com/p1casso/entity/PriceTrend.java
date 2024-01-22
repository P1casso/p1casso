package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格走势记录
 *
 * @TableName price_trend
 */
@TableName(value = "price_trend")
@Data
public class PriceTrend implements Serializable {
    /**
     *
     */
    @TableId
    private String id;

    /**
     *
     */
    private String ornamentsId;


    private String ornamentsName;

    /**
     *
     */
    private BigDecimal price;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public PriceTrend(String ornamentsId, String ornamentsName, BigDecimal price) {
        this.ornamentsId = ornamentsId;
        this.ornamentsName = ornamentsName;
        this.price = price;
    }
}