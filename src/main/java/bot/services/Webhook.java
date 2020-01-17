package bot.services;

import bot.models.TradingViewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Webhook {
    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);

    @PostMapping(value = "/tradingview", consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
        LOGGER.info("\nGot request:" + alert.toString() + "\n");
    }

    @PostMapping(value = "/tradingview", consumes = "text/plain")
    public void tradingView(@RequestParam String message) {
        LOGGER.info("Got message:" + message);
    }
}