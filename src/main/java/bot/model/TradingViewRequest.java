package bot.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class TradingViewRequest {
    @NotBlank
    private String assetPair;
    @NotBlank
    private String timeframe;
    @NotBlank
    private String side;
}
