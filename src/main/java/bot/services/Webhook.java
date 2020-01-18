package bot.services;

import bot.handler.H1AlertHandler;
import bot.handler.M5AlertHandler;
import bot.models.TradingViewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class Webhook {
    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);
    private static final String M5ALERT = "FIVE_MINUTE";
    private static final String H1ALERT = "ONE_HOUR";

    @Autowired
    private M5AlertHandler m5AlertHandler;
    @Autowired
    private H1AlertHandler h1AlertHandler;
    private Map<LocalDateTime, TradingViewRequest> timeToRequestMap = new HashMap<>();



    @PostMapping(value = "/tradingview", consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
//        timeToRequestMap.put(alert.setRequestTime(LocalDateTime.now()))
        if (alert.getTimeframe().equals(M5ALERT)) {
            m5AlertHandler.handleAlert(alert);
        } else {
            h1AlertHandler.handleAlert(alert);
        }

//        System.out.println("Got request:" + alert.toString() + "\n List of cached requests:");
//        for (Map.Entry<LocalDateTime, TradingViewRequest> entry : timeToRequestMap.entrySet()) {
//            LOGGER.info(entry.getKey().toString() + ", request:" + entry.getValue().toString());
//        }
    }

    @PostMapping(value = "/tradingview", consumes = "text/plain")
    public void tradingView(@RequestParam String message) {
        LOGGER.info("Got message:" + message);
    }
}