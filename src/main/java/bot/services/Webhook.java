package bot.services;

import bot.models.TradingViewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class Webhook {
    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);

    private Map<LocalDateTime, TradingViewRequest> timeToRequestMap = new HashMap<>();

    @PostMapping(value = "/tradingview", consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
        timeToRequestMap.put(LocalDateTime.now(), alert);
        System.out.println("Got request:" + alert.toString() + "\n List of cached requests:\n");
        for (Map.Entry<LocalDateTime, TradingViewRequest> entry : timeToRequestMap.entrySet()) {
            LOGGER.info(entry.getKey().toString() + ", request:" + entry.getValue().toString());
        }
    }

    @PostMapping(value = "/tradingview", consumes = "text/plain")
    public void tradingView(@RequestParam String message) {
        LOGGER.info("Got message:" + message);
    }
}