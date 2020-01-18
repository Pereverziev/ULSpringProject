package bot.service;

import com.binance.api.client.BinanceApiMarginRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
class BorrowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private AssetService assetService;

    void borrowUsdt() {
        marginClient.borrow("USDT", assetService.getUsdtEquivalentForOrder().toString());
        LOGGER.info("Borrowing USDT:" + assetService.getUsdtEquivalentForOrder().toString());
    }

    void borrowAsset(String symbol) {
        LOGGER.info("Borrowing " + symbol + ":" + assetService.getUsdtEquivalentForOrder().divide(assetService.getLastPriceOfAssetPair(symbol.concat("USDT")), RoundingMode.DOWN));
        marginClient.borrow(symbol, assetService.getUsdtEquivalentForOrder().divide(assetService.getLastPriceOfAssetPair(symbol.concat("USDT")), RoundingMode.DOWN).toString());
    }

    public void repayAsset(String symbol, String quantity) {
        marginClient.repay(symbol, quantity);
        LOGGER.info("Repaying " + symbol + ":" + quantity);
    }

}