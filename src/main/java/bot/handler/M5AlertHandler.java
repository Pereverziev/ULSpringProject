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
        final String trendSide = assetService.getTrendSide(request.getAssetPair());
        final String previousSide = assetService.getLast5MSignalSideForAssetPair(request.getAssetPair());
        if (trendSide.equals(request.getSide()) && !request.getSide().equals(previousSide)) {
            orderService.makeOrder(request);
            assetService.replace(request);
        }
        if (!trendSide.equals(request.getSide())) {
            orderService.closePositionIfOneExists(request.getAssetPair().concat(request.getTimeframe()));
            assetService.replace(request);
        }
    }
}