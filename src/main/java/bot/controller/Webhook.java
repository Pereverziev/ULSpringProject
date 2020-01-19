package bot.controller;

import bot.handler.H1AlertHandler;
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
    private static final String H1ALERT = "ONE_HOUR";

    @Autowired
    private M5AlertHandler m5AlertHandler;
    @Autowired
    private H1AlertHandler h1AlertHandler;
    @Autowired
    private AssetService assetService;

    @PostMapping(value = "/tradingview", consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
        LOGGER.info("Got request:" + alert);
        if (alert.getTimeframe().equals(M5ALERT)) {
            m5AlertHandler.handleAlert(alert);
        } else if (alert.getTimeframe().equals(H1ALERT)) {
            h1AlertHandler.handleAlert(alert);
        }
    }

    @PostMapping(value = "/tradingview", consumes = "text/plain")
    public void tradingView(@RequestParam String message) {
        LOGGER.info("Got message:" + message);
    }
}