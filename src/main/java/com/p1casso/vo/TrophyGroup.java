package com.p1casso.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrophyGroup {
    private String trophySetVersion;
    private String trophyTitleName;
    private String trophyTitleIconUrl;
    private String trophyTitlePlatform;
    private Trophies definedTrophies;
    private List<TrophyGroups> trophyGroups;
}