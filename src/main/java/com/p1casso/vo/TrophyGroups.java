package com.p1casso.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrophyGroups {
    private String trophyGroupId;
    private String trophyGroupName;
    private String trophyGroupIconUrl;
    private Trophies definedTrophies;
    private Trophies earnedTrophies;
}
