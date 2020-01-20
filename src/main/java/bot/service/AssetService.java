package bot.service;

import bot.model.TradingViewRequest;
import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.MarginAssetBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

@Component
@ConfigurationProperties
public class AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final BigDecimal SIXTEEN = new BigDecimal(60);
    private static final String TREND_TIMEFRAME = "ONE_HOUR";
    private static final String FIVE_MINUTE = "FIVE_MINUTE";
    private static final String FORTY_FIVE_MINUTE = "FORTY_FIVE_MINUTE";

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private BinanceApiRestClient client;
    @Value("${usdtQuantityForOrder}")
    private Long usdtQuantityForOrder;
    private Map<String, String> trendMap1H;
    private Map<String, String> trendMap45M;
    private Map<String, String> trend5MMap = new HashMap<>();
    private Map<String, Integer> assetPairRounding;

    BigDecimal getLastPriceOfAssetPair(String assetPair) {
        return new BigDecimal(client.get24HrPriceStatistics(assetPair).getLastPrice());
    }

    public String getH1Side(String assetPair) {
        return trendMap1H.get(assetPair);
    }

    public String setLast1HSignalSideForAssetPair(String assetPair, String side) {
        return trendMap1H.put(assetPair, side);
    }

    public String getLast5MSignalSideForAssetPair(String assetPair) {
        return trend5MMap.get(assetPair);
    }

    public String setLast5MSignalSideForAssetPair(String assetPair, String side) {
        return trend5MMap.put(assetPair, side);
    }

    public String getLast45MSignalSideForAssetPair(String assetPair) {
        return trendMap45M.get(assetPair);
    }

    public void setLast45MSignalSideForAssetPair(String assetPair, String side) {
        trendMap45M.put(assetPair, side);
    }

    String getOrderQuantityForAssetPair(String assetPair) {
        BigDecimal response = getUsdtEquivalentForOrder().divide(getLastPriceOfAssetPair(assetPair), new MathContext(assetPairRounding.get(assetPair), RoundingMode.DOWN));
        if (assetPair.equals("BTCUSDT")) {
            response = response.round(new MathContext(assetPairRounding.get(assetPair)));
        }
        LOGGER.info("Order quantity for " + assetPair + " is " + response.toPlainString());
        return response.toPlainString();
    }

    BigDecimal getUsdtEquivalentForOrder() {
        return BigDecimal.valueOf(usdtQuantityForOrder);
    }

    Integer getRounding(String assetPairTimeframe) {
        return assetPairRounding.get(assetPairTimeframe);
    }

    public void setTrendMap1H(Map<String, String> trendMap1H) {
        this.trendMap1H = trendMap1H;
    }

    public void setTrendMap45M(Map<String, String> trendMap45M) {
        this.trendMap45M = trendMap45M;
    }

    public Map<String, String> getTrendMap1H() {
        return trendMap1H;
    }

    public Map<String, String> getTrendMap45M() {
        return trendMap45M;
    }

    public Map<String, Integer> getAssetPairRounding() {
        return assetPairRounding;
    }

    public void setAssetPairRounding(Map<String, Integer> assetPairRounding) {
        this.assetPairRounding = assetPairRounding;
    }
}