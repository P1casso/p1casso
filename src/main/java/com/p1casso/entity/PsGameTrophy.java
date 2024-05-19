package com.p1casso.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName ps_game_trophy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsGameTrophy implements Serializable {
    @TableId
    private String npcommunicationid;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String pnpcommunicationid;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String trophytitlename;
    /**
     *
     */
    @Size(max = -1, message = "编码长度不能超过-1")

    @Length(max = -1, message = "编码长度不能超过-1")
    private String trophytitleiconurl;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String trophytitleplatform;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String hastrophygroups;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String trophygroupid;
    /**
     *
     */
    @Size(max = 255, message = "编码长度不能超过255")

    @Length(max = 255, message = "编码长度不能超过255")
    private String trophygroupname;
    /**
     *
     */
    @Size(max = -1, message = "编码长度不能超过-1")

    @Length(max = -1, message = "编码长度不能超过-1")
    private String trophygroupiconurl;
    /**
     *
     */

    private Integer definedbronze;
    /**
     *
     */

    private Integer definedsilver;
    /**
     *
     */

    private Integer definedgold;
    /**
     *
     */

    private Integer definedplatinum;
    /**
     *
     */

    private Integer earnedbronze;
    /**
     *
     */

    private Integer earnedsilver;
    /**
     *
     */

    private Integer earnedgold;
    /**
     *
     */

    private Integer earnedplatinum;
    /**
     *
     */

    private Date lastupdateddatetime;
}
