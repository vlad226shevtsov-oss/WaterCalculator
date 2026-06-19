package watercalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WaterCalculatorTest {

    private static final double DELTA = 0.000_001;

    @Test
    void fiveMinuteShowerUsesSixtyLiters() {
        assertEquals(60, WaterCalculator.calculateShowerWater(5));
    }

    @Test
    void showerDurationMustBePositive() {
        assertThrows(IllegalArgumentException.class,
                () -> WaterCalculator.calculateShowerWater(0));
        assertThrows(IllegalArgumentException.class,
                () -> WaterCalculator.calculateShowerWater(-1));
    }

    @Test
    void energyIsCalculatedFromLiters() {
        assertEquals(2.0934, WaterCalculator.calculateEnergy(60), DELTA);
    }

    @Test
    void negativeWaterAmountIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> WaterCalculator.calculateEnergy(-1));
    }

    @Test
    void sevenMinuteShowerReportContainsExpectedValues() {
        WaterReport report = WaterCalculator.calculateReport(ConsumptionType.SHOWER, 7);

        assertEquals(ConsumptionType.SHOWER, report.type());
        assertEquals(84, report.liters());
        assertEquals(2.93076, report.energyKwh(), DELTA);
        assertEquals(24, report.possibleSavingLiters());
    }

    @Test
    void bathReportDoesNotSuggestShowerSavings() {
        WaterReport report = WaterCalculator.calculateReport(ConsumptionType.BATH, 0);

        assertEquals(120, report.liters());
        assertEquals(4.1868, report.energyKwh(), DELTA);
        assertEquals(0, report.possibleSavingLiters());
    }
}
