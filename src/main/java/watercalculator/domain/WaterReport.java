package watercalculator.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record WaterReport(
        ConsumptionType type,
        ConsumptionRating rating,
        int liters,
        BigDecimal energyKwh,
        BigDecimal waterCost,
        BigDecimal energyCost,
        BigDecimal totalCost,
        int possibleSavingLiters
) {
    public WaterReport {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(rating, "rating must not be null");
        requireNonNegative(liters, "liters");
        requireNonNegative(energyKwh, "energyKwh");
        requireNonNegative(waterCost, "waterCost");
        requireNonNegative(energyCost, "energyCost");
        requireNonNegative(totalCost, "totalCost");
        requireNonNegative(possibleSavingLiters, "possibleSavingLiters");
    }

    private static void requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
    }

    private static void requireNonNegative(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
    }
}
