package bot.handler;

import bot.model.TradingViewRequest;
import bot.service.AssetService;
import bot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class M5AlertHandler {

    @Autowired
    private AssetService assetService;
    @Autowired
    private OrderService orderService;

    public void handleAlert(TradingViewRequest request) {
        final String m30Side = assetService.getM30Side(request.getAssetPair());
        final String m15Side = assetService.getM15Side(request.getAssetPair());
        if (m30Side.equals(request.getSide()) && m15Side.equals(m30Side) && !orderService.isOpenPositionForAssetPair(request.getAssetPair())) {
            orderService.makeOrder(request);
        }
        if (!m30Side.equals(request.getSide()) || !m15Side.equals(request.getSide())) {
            orderService.closePositionIfOneExists(request.getAssetPair().concat(request.getTimeframe()));
        }
    }
}