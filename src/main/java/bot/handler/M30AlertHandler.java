package bot.handler;

import bot.model.TradingViewRequest;
import bot.service.AssetService;
import bot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class M30AlertHandler {

    @Autowired
    private AssetService assetService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private M5AlertHandler m5AlertHandler;

    public void handleAlert(TradingViewRequest request) {
        if (!assetService.getM30Side(request.getAssetPair()).equals(request.getSide())) {
            assetService.setM30Side(request.getAssetPair(), request.getSide());
        }
    }
}