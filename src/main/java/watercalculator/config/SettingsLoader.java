package watercalculator.config;

import watercalculator.domain.CalculatorSettings;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Currency;
import java.util.Objects;
import java.util.Properties;

public final class SettingsLoader {

    private static final String DEFAULT_RESOURCE = "/calculator.properties";

    public CalculatorSettings loadDefault() throws IOException {
        try (InputStream input = SettingsLoader.class.getResourceAsStream(DEFAULT_RESOURCE)) {
            if (input == null) {
                throw new IOException("Default settings resource not found: " + DEFAULT_RESOURCE);
            }
            return load(input);
        }
    }

    public CalculatorSettings load(Path path) throws IOException {
        Objects.requireNonNull(path, "path must not be null");
        try (InputStream input = Files.newInputStream(path)) {
            return load(input);
        }
    }

    CalculatorSettings load(InputStream input) throws IOException {
        Objects.requireNonNull(input, "input must not be null");
        Properties properties = new Properties();
        properties.load(input);

        return new CalculatorSettings(
                intProperty(properties, "shower.liters-per-minute"),
                intProperty(properties, "bath.liters"),
                intProperty(properties, "dishwasher.liters"),
                intProperty(properties, "shower.target-minutes"),
                intProperty(properties, "rating.low-limit"),
                intProperty(properties, "rating.normal-limit"),
                intProperty(properties, "rating.high-limit"),
                decimalProperty(properties, "water.cold-temperature-celsius"),
                decimalProperty(properties, "water.hot-temperature-celsius"),
                decimalProperty(properties, "energy.kwh-per-liter-degree"),
                decimalProperty(properties, "price.water-per-cubic-meter"),
                decimalProperty(properties, "price.energy-per-kwh"),
                Currency.getInstance(requiredProperty(properties, "currency"))
        );
    }

    private static int intProperty(Properties properties, String key) {
        try {
            return Integer.parseInt(requiredProperty(properties, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer property: " + key, e);
        }
    }

    private static BigDecimal decimalProperty(Properties properties, String key) {
        try {
            return new BigDecimal(requiredProperty(properties, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid decimal property: " + key, e);
        }
    }

    private static String requiredProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return value.trim();
    }
}
