package com.p1casso.vo; /**
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
public class TrophyTitles {
    private String npServiceName;
    private String npCommunicationId;
    private String trophySetVersion;
    private String trophyTitleName;
    private String trophyTitleIconUrl;
    private String trophyTitlePlatform;
    private boolean hasTrophyGroups;
    private Trophies definedTrophies;
    private int progress;
    private Trophies earnedTrophies;
    private boolean hiddenFlag;
    private Date lastUpdatedDateTime;
}
