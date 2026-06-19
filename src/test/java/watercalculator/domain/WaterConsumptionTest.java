package watercalculator.domain;

import org.junit.jupiter.api.Test;
import watercalculator.support.TestSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WaterConsumptionTest {

    @Test
    void showerUsesConfiguredFlowRate() {
        assertEquals(84, new Shower(7).liters(TestSettings.defaults()));
    }

    @Test
    void bathUsesConfiguredVolume() {
        assertEquals(120, new Bath().liters(TestSettings.defaults()));
    }

    @Test
    void showerDurationMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new Shower(0));
        assertThrows(IllegalArgumentException.class, () -> new Shower(-1));
    }
}
