package watercalculator.ui;

import org.junit.jupiter.api.Test;
import watercalculator.application.WaterCalculatorService;
import watercalculator.domain.CalculatorSettings;
import watercalculator.support.TestSettings;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleApplicationTest {

    @Test
    void russianShowerScenarioProducesLocalizedReport() {
        CalculatorSettings settings = TestSettings.defaults();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (Scanner scanner = new Scanner("душ\n7\nда\n");
             PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            new ConsoleApplication(
                    scanner,
                    output,
                    new WaterCalculatorService(settings),
                    settings,
                    new Messages(Locale.forLanguageTag("ru"))
            ).run();
        }

        String result = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Расход воды: 84 л"));
        assertTrue(result.contains("24 л воды"));
        assertTrue(result.contains("40 л воды"));
        assertTrue(result.contains("Общая стоимость"));
    }

    @Test
    void excessiveShowerDurationIsRejectedBeforeIntegerOverflow() {
        CalculatorSettings settings = TestSettings.defaults();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (Scanner scanner = new Scanner("dusche\n2147483647\n7\nnein\n");
             PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            new ConsoleApplication(
                    scanner,
                    output,
                    new WaterCalculatorService(settings),
                    settings,
                    new Messages(Locale.GERMAN)
            ).run();
        }

        String result = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Diese Zahl ist zu groß"));
        assertTrue(result.contains("Wasserverbrauch: 84 Liter"));
    }
}
