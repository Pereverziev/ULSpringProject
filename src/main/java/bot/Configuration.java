package bot;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class Configuration {

    @Value("${apiKey}")
    private String apiKey;
    @Value("${secret}")
    private String secret;

    @Bean
    public BinanceApiMarginRestClient binanceApiMarginRestClient() {
        return BinanceApiClientFactory.newInstance(apiKey, secret).newMarginRestClient();
    }

    @Bean
    public BinanceApiRestClient binanceApiRestClient() {
        return BinanceApiClientFactory.newInstance(apiKey, secret).newRestClient();
    }
}