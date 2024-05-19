package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName ps_trophy
 */
@TableName(value = "ps_trophy")
@Data
public class PsTrophy implements Serializable {

    /**
     *
     */
    @TableId
    private String id;

    /**
     *
     */
    private String npcommunicationid;


    private String trophyGroupId;

    /**
     *
     */
    private Integer trophyid;

    /**
     *
     */
    private String trophytype;

    /**
     *
     */
    private String trophyname;

    /**
     *
     */
    private String trophydetail;

    /**
     *
     */
    private String trophyiconurl;

    /**
     *
     */
    private Integer earned;

    /**
     *
     */
    private Date earneddatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}