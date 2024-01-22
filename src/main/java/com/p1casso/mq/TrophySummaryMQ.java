package com.p1casso.mq;

import com.p1casso.service.impl.PsAccountServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class TrophySummaryMQ {

    @Resource
    private PsAccountServiceImpl psAccountService;

    @RabbitListener(queues = "direct.ps.trophyTitles")
    public void listenDirectQueue1() throws IOException {
        psAccountService.getTrophyTitles();
    }
}
