package watercalculator.domain;

import java.util.Objects;

public record Shower(int minutes) implements WaterConsumption {

    public Shower {
        if (minutes <= 0) {
            throw new IllegalArgumentException("minutes must be positive");
        }
    }

    @Override
    public ConsumptionType type() {
        return ConsumptionType.SHOWER;
    }

    @Override
    public int liters(CalculatorSettings settings) {
        Objects.requireNonNull(settings, "settings must not be null");
        return Math.multiplyExact(minutes, settings.showerLitersPerMinute());
    }
}
