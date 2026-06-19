package watercalculator;

import java.util.Objects;

public final class WaterCalculator {

    public static final int DISHWASHER_LITERS = 40;
    public static final int TARGET_SHOWER_MINUTES = 5;

    private static final int SHOWER_LITERS_PER_MINUTE = 12;
    private static final int BATH_LITERS = 120;
    private static final double TEMPERATURE_DIFFERENCE = 30.0;
    private static final double KWH_PER_LITER_DEGREE = 0.001163;

    private WaterCalculator() {
    }

    public static WaterReport calculateReport(ConsumptionType type, int showerMinutes) {
        Objects.requireNonNull(type, "type must not be null");

        int liters = switch (type) {
            case SHOWER -> calculateShowerWater(showerMinutes);
            case BATH -> BATH_LITERS;
        };
        double energyKwh = calculateEnergy(liters);
        int savingLiters = type == ConsumptionType.SHOWER
                ? Math.max(0, liters - calculateShowerWater(TARGET_SHOWER_MINUTES))
                : 0;

        return new WaterReport(type, liters, energyKwh, savingLiters);
    }

    public static int calculateShowerWater(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("minutes must be positive");
        }
        return minutes * SHOWER_LITERS_PER_MINUTE;
    }

    public static double calculateEnergy(int liters) {
        if (liters < 0) {
            throw new IllegalArgumentException("liters must not be negative");
        }
        return liters * KWH_PER_LITER_DEGREE * TEMPERATURE_DIFFERENCE;
    }
}
