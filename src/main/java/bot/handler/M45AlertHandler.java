package bot.handler;

import bot.model.TradingViewRequest;
import bot.service.AssetService;
import bot.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class M45AlertHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(M45AlertHandler.class);

    @Autowired
    private AssetService assetService;
    @Autowired
    private M5AlertHandler m5AlertHandler;
    @Autowired
    private OrderService orderService;

    public void handle(TradingViewRequest request) {
        final String side = assetService.getLast45MSignalSideForAssetPair(request.getAssetPair());
        if (!request.getSide().equals(side)) {
            assetService.setLast45MSignalSideForAssetPair(request.getAssetPair(), request.getSide());
            orderService.closePositionIfOneExists(request.getAssetPair().concat("FIVE_MINUTE"));
        }
    }

}