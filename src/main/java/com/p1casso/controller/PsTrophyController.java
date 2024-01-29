package com.p1casso.controller;

import com.p1casso.Utils.Result;
import com.p1casso.entity.PsAccount;
import com.p1casso.entity.PsTrophy;
import com.p1casso.service.impl.PsTrophyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ps/trophy")
public class PsTrophyController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private PsTrophyServiceImpl psTrophyService;

    @GetMapping("/update")
    public Result<PsAccount> InsertOrUpdateTrophy() {
        String exchangeName = "p1casso.ps.direct";
        rabbitTemplate.convertAndSend(exchangeName, "trophyList", "");
        return Result.success();
    }

    @GetMapping("/trophy_list/{npCommunicationId}")
    public Result<List<PsTrophy>> getTrophyListByNpCommunicationId(@PathVariable(value = "npCommunicationId") String id) {
        List<PsTrophy> trophyList = psTrophyService.getTrophyListByNpCommunicationId(id);
        return Result.success(trophyList);
    }

}
