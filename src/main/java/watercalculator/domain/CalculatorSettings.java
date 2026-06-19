package watercalculator.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record CalculatorSettings(
        int showerLitersPerMinute,
        int bathLiters,
        int dishwasherLiters,
        int targetShowerMinutes,
        int lowConsumptionLimit,
        int normalConsumptionLimit,
        int highConsumptionLimit,
        BigDecimal coldWaterTemperatureCelsius,
        BigDecimal hotWaterTemperatureCelsius,
        BigDecimal kwhPerLiterDegree,
        BigDecimal waterPricePerCubicMeter,
        BigDecimal energyPricePerKwh,
        Currency currency
) {
    public CalculatorSettings {
        requirePositive(showerLitersPerMinute, "showerLitersPerMinute");
        requirePositive(bathLiters, "bathLiters");
        requirePositive(dishwasherLiters, "dishwasherLiters");
        requirePositive(targetShowerMinutes, "targetShowerMinutes");
        requirePositive(lowConsumptionLimit, "lowConsumptionLimit");
        requirePositive(normalConsumptionLimit, "normalConsumptionLimit");
        requirePositive(highConsumptionLimit, "highConsumptionLimit");

        if (!(lowConsumptionLimit < normalConsumptionLimit
                && normalConsumptionLimit < highConsumptionLimit)) {
            throw new IllegalArgumentException("consumption limits must be strictly increasing");
        }

        Objects.requireNonNull(coldWaterTemperatureCelsius, "coldWaterTemperatureCelsius");
        Objects.requireNonNull(hotWaterTemperatureCelsius, "hotWaterTemperatureCelsius");
        requirePositive(kwhPerLiterDegree, "kwhPerLiterDegree");
        requireNonNegative(waterPricePerCubicMeter, "waterPricePerCubicMeter");
        requireNonNegative(energyPricePerKwh, "energyPricePerKwh");
        Objects.requireNonNull(currency, "currency must not be null");

        if (hotWaterTemperatureCelsius.compareTo(coldWaterTemperatureCelsius) <= 0) {
            throw new IllegalArgumentException("hot water temperature must exceed cold water temperature");
        }
    }

    public BigDecimal temperatureDifferenceCelsius() {
        return hotWaterTemperatureCelsius.subtract(coldWaterTemperatureCelsius);
    }

    private static void requirePositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private static void requirePositive(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private static void requireNonNegative(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
    }
}
