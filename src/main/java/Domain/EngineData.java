package Domain;

import Domain.enums.FlowRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineData {

    private double intermix;
    private FlowRate flowRate;
}
