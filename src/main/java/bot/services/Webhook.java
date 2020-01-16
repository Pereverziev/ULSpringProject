package bot.services;

import bot.models.TradingViewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/tradingview")
public class Webhook {

    @PostConstruct
    public void init() {
        LOGGER.info("hello world");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);

    @PostMapping(consumes = "application/json")
    public void tradingView(@RequestBody TradingViewRequest alert) {
        LOGGER.info("Got request:\n" + alert.toString());
    }

    @PostMapping(consumes = "text/plain;charset=utf-8")
    public void tradingView(String message) {
        LOGGER.info("Got message:" + message);
    }
}
