package watercalculator.application;

import watercalculator.domain.CalculatorSettings;
import watercalculator.domain.ConsumptionRating;
import watercalculator.domain.Shower;
import watercalculator.domain.WaterConsumption;
import watercalculator.domain.WaterReport;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;
import java.util.Objects;

public final class WaterCalculatorService {

    private static final BigDecimal LITERS_PER_CUBIC_METER = new BigDecimal("1000");
    private static final MathContext CALCULATION_CONTEXT = MathContext.DECIMAL64;

    private final CalculatorSettings settings;

    public WaterCalculatorService(CalculatorSettings settings) {
        this.settings = Objects.requireNonNull(settings, "settings must not be null");
    }

    public WaterReport calculate(WaterConsumption consumption) {
        return calculate(consumption, settings.currency());
    }

    public WaterReport calculate(WaterConsumption consumption, Currency targetCurrency) {
        Objects.requireNonNull(consumption, "consumption must not be null");
        Objects.requireNonNull(targetCurrency, "targetCurrency must not be null");

        int liters = consumption.liters(settings);
        BigDecimal energyKwh = calculateEnergy(liters);
        BigDecimal exchangeRate = settings.exchangeRate(targetCurrency);
        BigDecimal waterCost = calculateWaterCost(liters)
                .multiply(exchangeRate, CALCULATION_CONTEXT);
        BigDecimal energyCost = energyKwh
                .multiply(settings.energyPricePerKwh(), CALCULATION_CONTEXT)
                .multiply(exchangeRate, CALCULATION_CONTEXT);
        BigDecimal totalCost = waterCost.add(energyCost, CALCULATION_CONTEXT);

        return new WaterReport(
                consumption.type(),
                determineRating(liters),
                liters,
                energyKwh,
                waterCost,
                energyCost,
                totalCost,
                calculatePossibleSaving(consumption, liters)
        );
    }

    private BigDecimal calculateEnergy(int liters) {
        return BigDecimal.valueOf(liters)
                .multiply(settings.kwhPerLiterDegree(), CALCULATION_CONTEXT)
                .multiply(settings.temperatureDifferenceCelsius(), CALCULATION_CONTEXT);
    }

    private BigDecimal calculateWaterCost(int liters) {
        BigDecimal cubicMeters = BigDecimal.valueOf(liters)
                .divide(LITERS_PER_CUBIC_METER, CALCULATION_CONTEXT);
        return cubicMeters.multiply(settings.waterPricePerCubicMeter(), CALCULATION_CONTEXT);
    }

    private int calculatePossibleSaving(WaterConsumption consumption, int liters) {
        if (!(consumption instanceof Shower)) {
            return 0;
        }
        int targetLiters = new Shower(settings.targetShowerMinutes()).liters(settings);
        return Math.max(0, liters - targetLiters);
    }

    private ConsumptionRating determineRating(int liters) {
        if (liters <= settings.lowConsumptionLimit()) {
            return ConsumptionRating.LOW;
        }
        if (liters <= settings.normalConsumptionLimit()) {
            return ConsumptionRating.NORMAL;
        }
        if (liters <= settings.highConsumptionLimit()) {
            return ConsumptionRating.HIGH;
        }
        return ConsumptionRating.VERY_HIGH;
    }
}
