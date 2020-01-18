package bot.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
