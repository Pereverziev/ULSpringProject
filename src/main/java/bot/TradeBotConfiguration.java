package bot;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties
@PropertySource("classpath:configuration.properties")
public class TradeBotConfiguration {

    @Value("${apiKey}")
    private String apiKey;
    @Value("${secret}")
    private String secret;

    @Bean
    public BinanceApiMarginRestClient binanceApiMarginRestClient() {
        final BinanceApiClientFactory binanceApiClientFactory = BinanceApiClientFactory.newInstance(apiKey, secret);
        final BinanceApiMarginRestClient binanceApiMarginRestClient = binanceApiClientFactory.newMarginRestClient();
        binanceApiMarginRestClient.getAccount().setBorrowEnabled(true);
        return binanceApiMarginRestClient;
    }

    @Bean
    public BinanceApiRestClient binanceApiRestClient() {
        final BinanceApiClientFactory binanceApiClientFactory = BinanceApiClientFactory.newInstance(apiKey, secret);
        return binanceApiClientFactory.newRestClient();
    }
}