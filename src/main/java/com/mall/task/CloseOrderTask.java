package com.mall.task;

import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时关单
 * @author panjing
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ?") //每1分钟（每个一分钟的整数倍）
    public void closeOrderTask(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"), 2);
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

}
