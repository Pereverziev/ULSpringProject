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
        final String h1Side = assetService.getH1Side(request.getAssetPair());
        final String m45Side = assetService.getLast45MSignalSideForAssetPair(request.getAssetPair());
        final String previousM5Side = assetService.getLast5MSignalSideForAssetPair(request.getAssetPair());
        if (h1Side.equals(request.getSide()) && !request.getSide().equals(previousM5Side) && m45Side.equals(h1Side)) {
            orderService.makeOrder(request);
        }
        if (!h1Side.equals(request.getSide())) {
            orderService.closePositionIfOneExists(request.getAssetPair().concat(request.getTimeframe()));
        }
    }
}