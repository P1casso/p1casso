package com.p1casso.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.p1casso.Utils.Result;
import com.p1casso.entity.PsAccount;
import com.p1casso.entity.PsGameTrophy;
import com.p1casso.enums.ConsoleEnum;
import com.p1casso.exception.P1cassoException;
import com.p1casso.service.impl.PsAccountServiceImpl;
import com.p1casso.service.impl.PsGameTrophyServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/ps")
public class PsController {

    @Resource
    private PsAccountServiceImpl psAccountService;

    @Resource
    private PsGameTrophyServiceImpl psGameTrophyService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/login")
    public Result<String> login() throws IOException {
        Map<String, String> login = psAccountService.login();
        System.err.println(login);
        return Result.success();
    }

    @GetMapping("/refresh_trophy_statistics")
    public Result<String> refreshTrophyStatistics() {
        try {
            psAccountService.refreshTrophyStatistics();
        } catch (Exception e) {
            throw new P1cassoException(e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/trophy_statistics")
    public Result<PsAccount> getTrophyStatistics() {
        return Result.success(psAccountService.getTrophyStatistics());
    }

    @GetMapping("/test")
    public Result<PsAccount> test() throws IOException {
        psAccountService.updateGroupsTrophy("NPWR12518_00", ConsoleEnum.PS5);
        return Result.success();
    }

    @GetMapping("/trophy_titles")
    public Result<String> getAndReTrophyTitles() {
        String exchangeName = "p1casso.ps.direct";
        rabbitTemplate.convertAndSend(exchangeName, "trophyTitles", "");
        //psAccountService.getTrophyTitles();
        return Result.success();
    }

    @GetMapping("/game_list")
    public Result<List<Tree<String>>> getGameList() {
        QueryWrapper<PsGameTrophy> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("lastUpdatedDateTime");
        List<PsGameTrophy> list = psGameTrophyService.list(queryWrapper);
        TreeNodeConfig config = new TreeNodeConfig();
        List<Tree<String>> trees = TreeUtil.build(list, "0", config, (object, tree) -> {
            tree.setId(object.getNpcommunicationid());
            tree.setParentId(object.getPnpcommunicationid());
            tree.putExtra("platform", object.getTrophytitleplatform());
            tree.putExtra("definedBronze", object.getEarnedbronze() + "/" + object.getDefinedbronze());
            tree.putExtra("definedSilver", object.getEarnedsilver() + "/" + object.getDefinedsilver());
            tree.putExtra("definedGold", object.getEarnedgold() + "/" + object.getDefinedgold());
            tree.putExtra("definedPlatinum", object.getEarnedplatinum() + "/" + object.getDefinedplatinum());
            if (Objects.equals(object.getPnpcommunicationid(), "0")) {
                tree.setName(object.getTrophytitlename());
                tree.putExtra("imgUrl", object.getTrophytitleiconurl());

            } else {
                tree.setName(object.getTrophygroupname());
                tree.putExtra("imgUrl", object.getTrophygroupiconurl());
            }
            if (object.getEarnedplatinum() == null) {
                tree.putExtra("isPlatinum", 0);
            } else if (object.getEarnedplatinum() == 1 && object.getDefinedplatinum() == 1) {
                tree.putExtra("isPlatinum", 1);
            } else if (object.getDefinedplatinum() == 1 && object.getEarnedplatinum() == 0) {
                tree.putExtra("isPlatinum", 0);
            } else if (object.getDefinedplatinum() == 0) {
                tree.putExtra("isPlatinum", 2);
            } else {
                tree.putExtra("isPlatinum", 2);
            }

        });
        return Result.success(trees);
    }

}
