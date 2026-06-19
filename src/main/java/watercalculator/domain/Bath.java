package watercalculator.domain;

import java.util.Objects;

public record Bath() implements WaterConsumption {

    @Override
    public ConsumptionType type() {
        return ConsumptionType.BATH;
    }

    @Override
    public int liters(CalculatorSettings settings) {
        Objects.requireNonNull(settings, "settings must not be null");
        return settings.bathLiters();
    }
}
