package Domain;

import Domain.enums.Additive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineRequest {

    private AdjustmentRequest adjustmentRequest;
    private Additive additive;
}
