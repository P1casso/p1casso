package com.p1casso.mq;

import com.p1casso.service.impl.PsAccountServiceImpl;
import com.p1casso.service.impl.PsTrophyServiceImpl;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class TrophySummaryMQ {

    @Resource
    private PsAccountServiceImpl psAccountService;

    @Resource
    private PsTrophyServiceImpl psTrophyService;

    @RabbitListener(queues = "direct.ps.trophyTitles")
    public void listenTrophyTitles() {
        psAccountService.getTrophyTitles();
        psAccountService.updateGroupsTrophy();
    }

    @RabbitListener(queues = "direct.ps.trophyList")
    public void listenTrophyList() {
        System.err.println("调用");
        psTrophyService.InsertOrUpdateTrophy();
        psTrophyService.InsertOrUpdateTrophyEarnedState();
    }
}
