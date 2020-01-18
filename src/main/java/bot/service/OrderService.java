package bot.service;

import bot.model.TradingViewRequest;
import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final BigDecimal NINETEEN_EIGHT = new BigDecimal(98);

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private AssetService assetService;
    @Autowired
    private BorrowService borrowService;
    private Map<String, NewOrderResponse> assetPairTimeframeToOpenPositionMap = new HashMap<>();

    public void makeOrder(TradingViewRequest request) {
        if (request.getSide().equals("BUY")) {
            borrowService.borrowUsdt();
        } else if (request.getSide().equals("SELL")) {
            borrowService.borrowAsset(request.getAssetPair().subSequence(0, request.getAssetPair().length() - 4).toString());
        }
        final String quantity = assetService.getOrderQuantityForAssetPair(request.getAssetPair());
        final NewOrder newOrder = new NewOrder(request.getAssetPair(), OrderSide.valueOf(request.getSide()), OrderType.MARKET, null, quantity);
        LOGGER.info("Sending order:" + newOrder);
        final NewOrderResponse newOrderResponse = marginClient.newOrder(newOrder);
        assetPairTimeframeToOpenPositionMap.put(request.getAssetPair().concat(request.getTimeframe()), newOrderResponse);
    }

    public void closePositionIfOneExists(String assetPairTimeframe) {
        final NewOrderResponse position = assetPairTimeframeToOpenPositionMap.get(assetPairTimeframe);
        if (position != null) {
            final BigDecimal returnAmount = new BigDecimal(position.getExecutedQty()).multiply(NINETEEN_EIGHT).divide(ONE_HUNDRED, new MathContext(assetService.getRounding(position.getSymbol())));
            final NewOrder newOrder = new NewOrder(position.getSymbol(), position.getSide().equals(OrderSide.BUY) ? OrderSide.SELL : OrderSide.BUY, OrderType.MARKET, null, returnAmount.toString());
            final NewOrderResponse newOrderResponse = marginClient.newOrder(newOrder);
            LOGGER.info("Closing position " + position);
            if (position.getSide().toString().equals("BUY")) { // if trade we want to close is BUY type, LONG, i need to repay USDT debt.
                final BigDecimal loan = new BigDecimal(newOrderResponse.getExecutedQty()).multiply(assetService.getLastPriceOfAssetPair(position.getSymbol()));
                borrowService.repayAsset("USDT", loan.toString());
            } else {
                borrowService.repayAsset(position.getSymbol().subSequence(0, position.getSymbol().length() - 4).toString(), newOrderResponse.getExecutedQty());
            }
            assetPairTimeframeToOpenPositionMap.remove(assetPairTimeframe);
        }
    }
}