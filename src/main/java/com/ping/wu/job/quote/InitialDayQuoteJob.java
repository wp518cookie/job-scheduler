package com.ping.wu.job.quote;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ping.wu.quote.center.api.QuoteService;
import com.ping.wu.quote.center.api.model.quote.QuoteRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


/**
 * @author wuping
 * @date 2018/7/29
 */

@Component
public class InitialDayQuoteJob implements SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialDayQuoteJob.class);
    private final QuoteService quoteService;

    @Autowired
    public InitialDayQuoteJob(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public void execute(ShardingContext context) {
        try {
            LOGGER.info("----InitialHistoryQuoteJob start!");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date date = calendar.getTime();
            if (quoteService.checkTradeDay(date)) {
                quoteService.syncPerDayQuote(date);
                LOGGER.info("----InitialHistoryQuoteJob start success!");
            } else {
                LOGGER.info("----" + date.toString() + "不是交易日!不进行初始化的操作！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
