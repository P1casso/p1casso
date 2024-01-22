package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName ps_account
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ps_account")
public class PsAccount implements Serializable {
    private String accountid;

    private String token;

    private Date tokenExpirationTime;

    private String refreshToken;

    private Date refreshTokenExpirationTime;

    private Integer trophylevel;

    private Integer bronze;

    private Integer silver;

    private Integer gold;

    private Integer platinum;

    private String npsso;

    public PsAccount(Integer trophylevel, Integer bronze, Integer silver, Integer gold, Integer platinum) {
        this.trophylevel = trophylevel;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.platinum = platinum;
    }

    private static final long serialVersionUID = 1L;
}