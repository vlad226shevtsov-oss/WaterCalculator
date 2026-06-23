package watercalculator.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
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
        Currency currency,
        Map<Currency, BigDecimal> exchangeRates
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
        Objects.requireNonNull(exchangeRates, "exchangeRates must not be null");
        exchangeRates = Map.copyOf(exchangeRates);
        if (!exchangeRates.containsKey(currency)) {
            throw new IllegalArgumentException("exchange rate for base currency is required");
        }
        exchangeRates.forEach((targetCurrency, rate) -> {
            Objects.requireNonNull(targetCurrency, "exchange rate currency must not be null");
            requirePositive(rate, "exchange rate for " + targetCurrency);
        });

        if (hotWaterTemperatureCelsius.compareTo(coldWaterTemperatureCelsius) <= 0) {
            throw new IllegalArgumentException("hot water temperature must exceed cold water temperature");
        }
    }

    public BigDecimal temperatureDifferenceCelsius() {
        return hotWaterTemperatureCelsius.subtract(coldWaterTemperatureCelsius);
    }

    public BigDecimal exchangeRate(Currency targetCurrency) {
        Objects.requireNonNull(targetCurrency, "targetCurrency must not be null");
        BigDecimal rate = exchangeRates.get(targetCurrency);
        if (rate == null) {
            throw new IllegalArgumentException("unsupported currency: " + targetCurrency);
        }
        return rate;
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
