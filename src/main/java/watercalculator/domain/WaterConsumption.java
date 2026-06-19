package watercalculator.domain;

public interface WaterConsumption {

    ConsumptionType type();

    int liters(CalculatorSettings settings);
}
