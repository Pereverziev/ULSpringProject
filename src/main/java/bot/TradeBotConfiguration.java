package bot;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiMarginRestClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@PropertySource("classpath:configuration.properties")
public class TradeBotConfiguration {
    @Bean
    public BinanceApiMarginRestClient binanceApiMarginRestClient() {
        final BinanceApiClientFactory binanceApiClientFactory = BinanceApiClientFactory.newInstance("K6nkiksUgEZcRyf0e1UmcVShkzX6c8mpWuwJFNxYlgJRRUzBXTZYm36P11fbh5Wa", "OSew7LdGGee8m6ilBt1ru7fqhTgVgIXjXJctTycaeN7TSt2ZYJgRdpJCu8b78jI9");
        return binanceApiClientFactory.newMarginRestClient();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
