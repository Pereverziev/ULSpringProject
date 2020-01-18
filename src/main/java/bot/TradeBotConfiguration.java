package bot;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource("classpath:configuration.properties")
public class TradeBotConfiguration {

    @Bean
    public BinanceApiMarginRestClient binanceApiMarginRestClient() {
        final BinanceApiClientFactory binanceApiClientFactory = BinanceApiClientFactory.newInstance("K6nkiksUgEZcRyf0e1UmcVShkzX6c8mpWuwJFNxYlgJRRUzBXTZYm36P11fbh5Wa", "OSew7LdGGee8m6ilBt1ru7fqhTgVgIXjXJctTycaeN7TSt2ZYJgRdpJCu8b78jI9");
        return binanceApiClientFactory.newMarginRestClient();
    }

    @Bean
    public BinanceApiRestClient binanceApiRestClient() {
        final BinanceApiClientFactory binanceApiClientFactory = BinanceApiClientFactory.newInstance("K6nkiksUgEZcRyf0e1UmcVShkzX6c8mpWuwJFNxYlgJRRUzBXTZYm36P11fbh5Wa", "OSew7LdGGee8m6ilBt1ru7fqhTgVgIXjXJctTycaeN7TSt2ZYJgRdpJCu8b78jI9");
        return binanceApiClientFactory.newRestClient();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
