package bot.services;

import com.binance.api.client.BinanceApiMarginRestClient;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.MarginAssetBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AssetService {
    private static final String USDT_SUFFIX = "USDT";

    @Autowired
    private BinanceApiMarginRestClient marginClient;
    @Autowired
    private BinanceApiRestClient client;
    @Value("${assetPair}")
    private List<String> assetPairList;
    private Set<String> assetList;
//    private Map<String, MarginAssetBalance> assetToBalanceMap;

    @PostConstruct
    public void init() {
        System.out.println("New life");
        assetList = assetPairList.stream().flatMap(string -> Stream.of(string.subSequence(0, string.length() - 4).toString(), string.substring(string.length() - 4))).collect(Collectors.toSet());
//        for (String s : assetList) {
//            System.out.println(getByAssetName(s));
//        }
//        assetToBalanceMap =assetList.stream().collect(Collectors.toMap(Function.identity(), symbol -> marginClient.getAccount().getAssetBalance(symbol)));
    }

    public MarginAssetBalance getByAssetName(String asset) {
        return marginClient.getAccount().getAssetBalance(asset);
    }

    private BigDecimal getLastPriceOfAssetPair(String name) {
        return new BigDecimal(client.get24HrPriceStatistics(name).getLastPrice());
    }


}