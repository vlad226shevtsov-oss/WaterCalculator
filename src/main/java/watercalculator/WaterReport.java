package watercalculator;

import java.util.Objects;

public record WaterReport(
        ConsumptionType type,
        int liters,
        double energyKwh,
        int possibleSavingLiters
) {
    public WaterReport {
        Objects.requireNonNull(type, "type must not be null");
        if (liters < 0) {
            throw new IllegalArgumentException("liters must not be negative");
        }
        if (energyKwh < 0) {
            throw new IllegalArgumentException("energyKwh must not be negative");
        }
        if (possibleSavingLiters < 0) {
            throw new IllegalArgumentException("possibleSavingLiters must not be negative");
        }
    }
}
