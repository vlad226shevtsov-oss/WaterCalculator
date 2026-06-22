package watercalculator.domain;

import org.junit.jupiter.api.Test;
import watercalculator.support.TestSettings;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorSettingsTest {

    @Test
    void temperatureDifferenceIsDerived() {
        assertEquals(new BigDecimal("30"),
                TestSettings.defaults().temperatureDifferenceCelsius());
    }

    @Test
    void limitsMustBeStrictlyIncreasing() {
        CalculatorSettings defaults = TestSettings.defaults();

        assertThrows(IllegalArgumentException.class, () -> new CalculatorSettings(
                defaults.showerLitersPerMinute(),
                defaults.bathLiters(),
                defaults.dishwasherLiters(),
                defaults.targetShowerMinutes(),
                100,
                60,
                150,
                defaults.coldWaterTemperatureCelsius(),
                defaults.hotWaterTemperatureCelsius(),
                defaults.kwhPerLiterDegree(),
                defaults.waterPricePerCubicMeter(),
                defaults.energyPricePerKwh(),
                defaults.currency(),
                defaults.exchangeRates()
        ));
    }
}
