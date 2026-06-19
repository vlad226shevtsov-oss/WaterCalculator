package watercalculator.application;

import org.junit.jupiter.api.Test;
import watercalculator.domain.Bath;
import watercalculator.domain.ConsumptionRating;
import watercalculator.domain.ConsumptionType;
import watercalculator.domain.Shower;
import watercalculator.domain.WaterReport;
import watercalculator.support.TestSettings;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaterCalculatorServiceTest {

    private final WaterCalculatorService calculator =
            new WaterCalculatorService(TestSettings.defaults());

    @Test
    void sevenMinuteShowerCalculatesResourcesCostsAndSaving() {
        WaterReport report = calculator.calculate(new Shower(7));

        assertEquals(ConsumptionType.SHOWER, report.type());
        assertEquals(ConsumptionRating.NORMAL, report.rating());
        assertEquals(84, report.liters());
        assertDecimalEquals("2.930760", report.energyKwh());
        assertDecimalEquals("0.21000", report.waterCost());
        assertDecimalEquals("1.1723040", report.energyCost());
        assertDecimalEquals("1.3823040", report.totalCost());
        assertEquals(24, report.possibleSavingLiters());
    }

    @Test
    void bathUsesConfiguredVolumeAndHasNoShowerSaving() {
        WaterReport report = calculator.calculate(new Bath());

        assertEquals(ConsumptionType.BATH, report.type());
        assertEquals(ConsumptionRating.HIGH, report.rating());
        assertEquals(120, report.liters());
        assertDecimalEquals("4.186800", report.energyKwh());
        assertEquals(0, report.possibleSavingLiters());
    }

    @Test
    void shortShowerNeverProducesNegativeSaving() {
        assertEquals(0, calculator.calculate(new Shower(3)).possibleSavingLiters());
    }

    private static void assertDecimalEquals(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }
}
