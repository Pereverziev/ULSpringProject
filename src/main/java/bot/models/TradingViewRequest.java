package bot.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors
public class TradingViewRequest {
    @NotBlank
    private String assetPair;
    @NotBlank
    private String timeframe;
    @NotBlank
    private String side;
}
