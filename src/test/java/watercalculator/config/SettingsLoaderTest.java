package watercalculator.config;

import org.junit.jupiter.api.Test;
import watercalculator.domain.CalculatorSettings;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SettingsLoaderTest {

    @Test
    void defaultSettingsAreLoadedFromClasspath() throws Exception {
        CalculatorSettings settings = new SettingsLoader().loadDefault();

        assertEquals(12, settings.showerLitersPerMinute());
        assertEquals(120, settings.bathLiters());
        assertEquals("EUR", settings.currency().getCurrencyCode());
        assertEquals(0, new java.math.BigDecimal("51.4631").compareTo(
                settings.exchangeRate(Currency.getInstance("UAH"))));
    }

    @Test
    void invalidPropertyIsReported() {
        String properties = """
                shower.liters-per-minute=not-a-number
                """;

        assertThrows(IllegalArgumentException.class, () -> new SettingsLoader().load(
                new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8))
        ));
    }
}
