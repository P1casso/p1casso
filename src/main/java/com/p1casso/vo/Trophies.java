package com.p1casso.vo;

/**
 * Copyright 2024 json.cn
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Auto-generated: 2024-01-05 18:12:49
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trophies {
    private int bronze;
    private int silver;
    private int gold;
    private int platinum;
    private int trophyId;
    private boolean trophyHidden;
    private String trophyType;
    private String trophyName;
    private String trophyDetail;
    private String trophyIconUrl;
    private String trophyGroupId;
    private Boolean earned;
    private Integer trophyRare;
    private String trophyEarnedRate;
    private Date earnedDateTime;
}