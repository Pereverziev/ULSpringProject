package bot.controller;

import bot.handler.M30AlertHandler;
import bot.handler.M15AlertHandler;
import bot.handler.M5AlertHandler;
import bot.model.TradingViewRequest;
import bot.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Webhook {
    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);
    private static final String M5ALERT = "FIVE_MINUTE";
    private static final String M15ALERT = "FIFTEEN_MINUTE";
    private static final String M30ALERT = "THIRTEEN_MINUTE";

    @Autowired
    private M30AlertHandler m30AlertHandler;
    @Autowired
    private M15AlertHandler m15AlertHandler;
    @Autowired
    private M5AlertHandler m5AlertHandler;
    @Autowired
    private AssetService assetService;

    @PostMapping(value = "/tradingview", consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
        LOGGER.info("Got request:" + alert);
        switch (alert.getTimeframe()) {
            case M5ALERT:
                m5AlertHandler.handleAlert(alert);
                break;
            case M15ALERT:
                m15AlertHandler.handle(alert);
                break;
            case M30ALERT:
                m30AlertHandler.handleAlert(alert);
                break;
        }
    }

    @PostMapping(value = "/tradingview", consumes = "text/plain")
    public void tradingView(@RequestParam String message) {
        LOGGER.info("Got message:" + message);
    }
}