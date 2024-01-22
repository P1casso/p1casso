package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName ps_game_trophy
 */
@TableName(value = "ps_game_trophy")
@Data
public class PsGameTrophy implements Serializable {
    @TableId
    private String npcommunicationid;

    private String trophytitlename;

    private String trophytitleiconurl;

    private String trophytitleplatform;

    private String hastrophygroups;

    private Integer definedbronze;

    private Integer definedsilver;

    private Integer definedgold;

    private Integer definedplatinum;

    private Integer earnedbronze;

    private Integer earnedsilver;

    private Integer earnedgold;

    private Integer earnedplatinum;

    private Date lastupdateddatetime;

    private String pnpcommunicationid;

    private String trophygroupid;

    private String trophygroupname;

    private String trophygroupiconurl;

    private static final long serialVersionUID = 1L;
}