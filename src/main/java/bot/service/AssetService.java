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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ConfigurationProperties
public class AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final BigDecimal SIXTEEN = new BigDecimal(60);
    private static final String TREND_TIMEFRAME = "ONE_HOUR";
    private static final String FIVE_MINUTE = "FIVE_MINUTE";

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private BinanceApiRestClient client;
    @Value("${assetPair}")
    private List<String> assetPairList;
    @Value("${usdtQuantityForOrder}")
    private Long usdtQuantityForOrder;
    private Map<String, String> trendMap;
    private Map<String, Integer> assetPairRounding;
    private Map<String, TradingViewRequest> assetPairTimeframeToRequestsMap = new HashMap<>();

    @PostConstruct
    public void init() {
        assetPairList.forEach(symbolPair -> assetPairTimeframeToRequestsMap.put(symbolPair.concat(TREND_TIMEFRAME), new TradingViewRequest().setAssetPair(symbolPair).setSide(trendMap.get(symbolPair)).setTimeframe(TREND_TIMEFRAME)));
    }

    MarginAssetBalance getByAssetName(String asset) {
        return marginClient.getAccount().getAssetBalance(asset);
    }

    BigDecimal getLastPriceOfAssetPair(String name) {
        return new BigDecimal(client.get24HrPriceStatistics(name).getLastPrice());
    }

    public String getTrendSide(String assetPair) {
        return assetPairTimeframeToRequestsMap.get(assetPair.concat(TREND_TIMEFRAME)).getSide();
    }

    public String getLast5MSignalSideForAssetPair(String assetPair) {
        final TradingViewRequest tradingViewRequest = assetPairTimeframeToRequestsMap.get(assetPair.concat(FIVE_MINUTE));
        if (tradingViewRequest != null) {
            return tradingViewRequest.getSide();
        }
        return null;
    }

    String getOrderQuantityForAssetPair(String assetPair) {
        BigDecimal response = getUsdtEquivalentForOrder().divide(getLastPriceOfAssetPair(assetPair),new MathContext(assetPairRounding.get(assetPair)));
        if (assetPair.equals("BTCUSDT")) {
            response = response.round(new MathContext(assetPairRounding.get(assetPair)));
        }
        LOGGER.info("Order quantity for " + assetPair + " is " + response);
        return response.toString();
    }

    public void replace(TradingViewRequest request) {
        assetPairTimeframeToRequestsMap.put(request.getAssetPair().concat(request.getTimeframe()), request);
    }

    public void invalidate(String assertPairTimeframe) {
        assetPairTimeframeToRequestsMap.remove(assertPairTimeframe);
    }

    public BigDecimal getUsdtEquivalentForOrder() {
        return BigDecimal.valueOf(usdtQuantityForOrder);
    }

    public void setTrendMap(Map<String, String> trendMap) {
        this.trendMap = trendMap;
    }

    public Map<String, String> getTrendMap() {
        return trendMap;
    }

    public Map<String, Integer> getAssetPairRounding() {
        return assetPairRounding;
    }

    public void setAssetPairRounding(Map<String, Integer> assetPairRounding) {
        this.assetPairRounding = assetPairRounding;
    }

    public Integer getRounding(String assetPairTimeframe) {
        return assetPairRounding.get(assetPairTimeframe);
    }

//    private BigDecimal getNetDeposit() {
//        BigDecimal totalUsdtAltcoinDebt = BigDecimal.ZERO;
//        BigDecimal totalUsdtAltcoinBalance = BigDecimal.ZERO;
//        for (String symbol : assetList) {
//            if (!symbol.equals("USDT")) {
//                final MarginAssetBalance balance = getByAssetName(symbol);
//                totalUsdtAltcoinDebt = totalUsdtAltcoinDebt.add(new BigDecimal(balance.getBorrowed()).multiply(getLastPriceOfAssetPair(symbol.concat("USDT"))));
//                totalUsdtAltcoinBalance = totalUsdtAltcoinBalance.add(new BigDecimal(balance.getFree()).multiply(getLastPriceOfAssetPair(symbol.concat("USDT"))));
//            }
//        }
//        MarginAssetBalance usdtBalance = getByAssetName("USDT");
//        final BigDecimal freeUsdt = new BigDecimal(usdtBalance.getFree());
//        final BigDecimal borrowedUsdt = new BigDecimal(usdtBalance.getBorrowed());
//        return freeUsdt.subtract(totalUsdtAltcoinDebt).subtract(borrowedUsdt).add(totalUsdtAltcoinBalance);
//    }
}