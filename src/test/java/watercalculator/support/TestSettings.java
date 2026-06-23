package watercalculator.support;

import watercalculator.domain.CalculatorSettings;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public final class TestSettings {

    private TestSettings() {
    }

    public static CalculatorSettings defaults() {
        return new CalculatorSettings(
                12,
                120,
                40,
                5,
                60,
                100,
                150,
                new BigDecimal("10"),
                new BigDecimal("40"),
                new BigDecimal("0.001163"),
                new BigDecimal("2.50"),
                new BigDecimal("0.40"),
                Currency.getInstance("EUR"),
                Map.of(
                        Currency.getInstance("EUR"), BigDecimal.ONE,
                        Currency.getInstance("USD"), new BigDecimal("1.1467"),
                        Currency.getInstance("UAH"), new BigDecimal("51.4631")
                )
        );
    }
}
