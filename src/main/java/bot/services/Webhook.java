package bot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class Webhook {

    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);

    @PostMapping(path = "/tradingview")
    public void tradingView(@RequestBody Object alert) {
        LOGGER.info("Got request:\n" + alert.toString());
    }
}
