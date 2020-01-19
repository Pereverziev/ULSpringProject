package bot.service;

import com.binance.api.client.BinanceApiMarginRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        final String borrowAmount = assetService.getOrderQuantityForAssetPair(symbol.concat("USDT"));
        LOGGER.info("Borrowing " + symbol + ":" + borrowAmount);
        marginClient.borrow(symbol, borrowAmount);
    }

    public void repayAsset(String symbol, String quantity) {
        marginClient.repay(symbol, quantity);
        LOGGER.info("Repaying " + symbol + ":" + quantity);
    }

}