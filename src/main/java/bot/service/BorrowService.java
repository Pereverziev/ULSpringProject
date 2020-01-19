package bot.service;

import com.binance.api.client.BinanceApiMarginRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class BorrowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private AssetService assetService;

    String borrowUsdt(String assetPair) {
        final BigDecimal usdtEquivalentForOrder = assetService.getUsdtEquivalentForOrder();
        LOGGER.info("Borrowing USDT:" + usdtEquivalentForOrder.toString());
        marginClient.borrow("USDT", usdtEquivalentForOrder.toString());
        return assetService.getOrderQuantityForAssetPair(assetPair, usdtEquivalentForOrder);
    }

    String borrowAsset(String symbol) {
        final BigDecimal usdtEquivalentForOrder = assetService.getUsdtEquivalentForOrder();
        final String quantity = assetService.getOrderQuantityForAssetPair(symbol.concat("USDT"), usdtEquivalentForOrder);
        LOGGER.info("Borrowing " + symbol + ":" + quantity);
        marginClient.borrow(symbol, quantity);
        return quantity;
    }

    public void repayAsset(String symbol, String quantity) {
        LOGGER.info("Repaying " + symbol + ":" + quantity);
        marginClient.repay(symbol, quantity);
    }

}