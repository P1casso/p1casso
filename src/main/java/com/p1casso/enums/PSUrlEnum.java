package com.p1casso.enums;

import lombok.Getter;

@Getter
public enum PSUrlEnum {

    PRE_AUTHORIZATION("https://ca.account.sony.com/api/authz/v3/oauth/authorize?access_type=offline&client_id=09515159-7237-4370-9b40-3806e67c0891&redirect_uri=com.scee.psxandroid.scecompcall://redirect&response_type=code&scope=psn:mobile.v2.core psn:clientapp"),

    /**
     * 奖杯统计数据
     */
    TROPHY_STATISTICS("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/trophySummary"),

    TROPHY_TITLES("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/trophyTitles"),

    TROPHY_GROUPS_DEFINED_PS4("https://m.np.playstation.com/api/trophy/v1/npCommunicationIds/%s/trophyGroups?npServiceName=trophy"),

    TROPHY_GROUPS_DEFINED_PS5("https://m.np.playstation.com/api/trophy/v1/npCommunicationIds/%s/trophyGroups"),

    TROPHY_GROUPS_EARNED_PS5("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/npCommunicationIds/%s/trophyGroups"),

    TROPHY_GROUPS_EARNED_PS4("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/npCommunicationIds/%s/trophyGroups?npServiceName=trophy"),

    /**
     * 游戏奖杯列表（不包含获得情况）
     */
    TROPHY_LIST_PS5("https://m.np.playstation.com/api/trophy/v1/npCommunicationIds/%s/trophyGroups/all/trophies"),

    TROPHY_LIST_PS4("https://m.np.playstation.com/api/trophy/v1/npCommunicationIds/%s/trophyGroups/all/trophies?npServiceName=trophy"),

    /**
     * 游戏奖杯列表（包含获得情况）
     */
    TROPHY_LIST_EARNED_PS5("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/npCommunicationIds/%s/trophyGroups/all/trophies"),

    TROPHY_LIST_EARNED_PS4("https://m.np.playstation.com/api/trophy/v1/users/1744982702939037104/npCommunicationIds/%s/trophyGroups/all/trophies?npServiceName=trophy");

    private final String url;

    public String getUrl(String npCommunicatioinId) {
        return String.format(url, npCommunicatioinId);
    }

    PSUrlEnum(String url) {
        this.url = url;
    }
}
