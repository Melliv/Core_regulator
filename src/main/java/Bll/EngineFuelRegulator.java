package Bll;

import Domain.AdjustmentRequest;
import Domain.EngineData;
import Domain.EngineRequest;
import Domain.enums.Additive;
import Domain.enums.FlowRate;

public class EngineFuelRegulator {

    private final double MULTIPLIER = 3.5;
    private final double MAX_QUANTITY = 0.2;
    private final double FIXED_ADDITIVE_QUANTITY = 0.05;

    public EngineRequest getRequest(EngineData engineData) {
        EngineRequest engineRequest = new EngineRequest();
        AdjustmentRequest adjustmentRequest = new AdjustmentRequest();

        adjustmentRequest.setValue(calculateAdditiveQuantity(engineData));
        engineRequest.setAdditive(getAdditive(engineData));

        engineRequest.setAdjustmentRequest(adjustmentRequest);
        return engineRequest;
    }

    private double calculateAdditiveQuantity(EngineData engineData) {
        double quantity = Math.abs(engineData.getIntermix() - 0.5)
                / 0.5
                * MAX_QUANTITY
                * MULTIPLIER
                + FIXED_ADDITIVE_QUANTITY;

        if (engineData.getFlowRate() == FlowRate.HIGH) {
            quantity *= -1;
        }

        if (Math.abs(quantity) > MAX_QUANTITY) {
            quantity = (quantity > 0) ? MAX_QUANTITY : -MAX_QUANTITY;
        }

        return quantity;
    }

    private Additive getAdditive(EngineData engineData) {
        double intermix = engineData.getIntermix();
        FlowRate flowRate = engineData.getFlowRate();
        if ((intermix > 0.5 && flowRate != FlowRate.HIGH) ||
                (intermix < 0.5 && flowRate == FlowRate.HIGH)) {
            return Additive.antimatter;
        } else if (intermix < 0.5 || intermix > 0.5 || flowRate != FlowRate.OPTIMAL) {
            return Additive.matter;
        }
        return Additive.NONE;
    }
}
