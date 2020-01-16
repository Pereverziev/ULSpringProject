package bot.services;

import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.domain.account.MarginAssetBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AssetService {
    private static final Map<String, String> assetMap = java.util.Map.of("BTC", "USDT", "DASH", "BTC");

    @Autowired
    private BinanceApiMarginRestClient restClient;
    private Map<String, MarginAssetBalance> assetToBalanceMap;

    @PostConstruct
    public void init() {
        refreshCache();
        assetToBalanceMap.values().forEach(ss -> System.out.println(ss));
    }

    public MarginAssetBalance getByAssetName(String asset) {
        refreshCache();
        return assetToBalanceMap.get(asset);
    }

    private void refreshCache() {
        assetToBalanceMap = restClient.getAccount().getUserAssets().stream().filter(asset -> assetMap.containsKey(asset.getAsset()) ||
                assetMap.containsValue(asset.getAsset())).collect(Collectors.toMap(MarginAssetBalance::getAsset, Function.identity()));
    }

}
