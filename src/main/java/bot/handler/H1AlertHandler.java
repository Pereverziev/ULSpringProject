package bot.handler;

import bot.model.TradingViewRequest;
import bot.service.AssetService;
import bot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class H1AlertHandler {

    @Autowired
    private AssetService assetService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private M5AlertHandler m5AlertHandler;

    public void handleAlert(TradingViewRequest request) {
        if (!assetService.getTrendSide(request.getAssetPair()).equals(request.getSide())) {
            orderService.closePositionIfOneExists(request.getAssetPair().concat("FIVE_MINUTE"));
            assetService.invalidate(request.getAssetPair().concat("FIVE_MINUTE"));
            assetService.replace(request);
            m5AlertHandler.handleAlert(new TradingViewRequest().setAssetPair(request.getAssetPair()).setSide(request.getSide()).setTimeframe("FIVE_MINUTE"));
        }
    }
}