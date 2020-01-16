package bot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class Webhook {

    @PostConstruct
    public void init() {
        LOGGER.info("hello world");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Webhook.class);

    @PostMapping(path = "/tradingview")
    public void tradingView(@RequestBody Object alert) {
        LOGGER.info("Got request:\n" + alert.toString());
    }
}
