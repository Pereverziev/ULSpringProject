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

    String borrowUsdt(String assetPair) {
        LOGGER.info("Borrowing USDT:" + assetService.getUsdtEquivalentForOrder().toString());
        marginClient.borrow("USDT",  assetService.getUsdtEquivalentForOrder().toString());
        return assetService.getOrderQuantityForAssetPair(assetPair);
    }

    String borrowAsset(String symbol) {
        final String quantity = assetService.getOrderQuantityForAssetPair(symbol.concat("USDT"));
        LOGGER.info("Borrowing " + symbol + ":" + quantity);
        marginClient.borrow(symbol, quantity);
        return quantity;
    }

    void repayAsset(String symbol, String quantity) {
        LOGGER.info("Repaying " + symbol + ":" + quantity);
        marginClient.repay(symbol, quantity);
    }

}