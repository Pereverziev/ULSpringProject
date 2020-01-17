package bot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class TradeBotEntryPoint {

    public static void main(String[] args) {
        new SpringApplicationBuilder().main(TradeBotEntryPoint.class).sources(TradeBotConfiguration.class).run(args);
    }

}