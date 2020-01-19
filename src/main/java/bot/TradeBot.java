package bot;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class TradeBot {

    public static void main(String[] args) {
        new SpringApplicationBuilder().main(TradeBot.class).sources(TradeBotConfiguration.class).run(args);
    }

}