package bot.service;

import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

@Component
@ConfigurationProperties
public class AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetService.class);
    private static final String TREND_TIMEFRAME = "ONE_HOUR";
    private static final String FIVE_MINUTE = "FIVE_MINUTE";
    private static final String FORTY_FIVE_MINUTE = "FORTY_FIVE_MINUTE";

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private BinanceApiRestClient client;
    @Value("${usdtQuantityForOrder}")
    private Long usdtQuantityForOrder;
    private Map<String, String> trendMap30M;
    private Map<String, String> trendMap15M;
    private Map<String, Integer> assetPairRounding;

    BigDecimal getLastPriceOfAssetPair(String assetPair) {
        return new BigDecimal(client.get24HrPriceStatistics(assetPair).getLastPrice());
    }

    public String getM30Side(String assetPair) {
        return trendMap30M.get(assetPair);
    }

    public String setM30Side(String assetPair, String side) {
        return trendMap30M.put(assetPair, side);
    }

    public String getM15Side(String assetPair) {
        return trendMap15M.get(assetPair);
    }

    public void setM15Side(String assetPair, String side) {
        trendMap15M.put(assetPair, side);
    }

    String getOrderQuantityForAssetPair(String assetPair) {
        final BigDecimal response = getUsdtEquivalentForOrder().divide(getLastPriceOfAssetPair(assetPair), new MathContext(assetPairRounding.get(assetPair), RoundingMode.DOWN));
        LOGGER.info("Order quantity for " + assetPair + " is " + response.toPlainString());
        return response.toPlainString();
    }

    BigDecimal getUsdtEquivalentForOrder() {
        return BigDecimal.valueOf(usdtQuantityForOrder);
    }

    Integer getRounding(String assetPairTimeframe) {
        return assetPairRounding.get(assetPairTimeframe);
    }

    public void setTrendMap30M(Map<String, String> trendMap30M) {
        this.trendMap30M = trendMap30M;
    }

    public Map<String, String> getTrendMap30M() {
        return trendMap30M;
    }

    public void setTrendMap15M(Map<String, String> trendMap15M) {
        this.trendMap15M = trendMap15M;
    }

    public Map<String, String> getTrendMap15M() {
        return trendMap15M;
    }

    public Map<String, Integer> getAssetPairRounding() {
        return assetPairRounding;
    }

    public void setAssetPairRounding(Map<String, Integer> assetPairRounding) {
        this.assetPairRounding = assetPairRounding;
    }
}