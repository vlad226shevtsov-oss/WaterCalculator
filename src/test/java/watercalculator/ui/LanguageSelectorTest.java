package watercalculator.ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageSelectorTest {

    @Test
    void russianLanguageCanBeSelectedInteractively() {
        try (Scanner scanner = new Scanner(" RU \n");
             PrintStream output = new PrintStream(
                     new ByteArrayOutputStream(), true, StandardCharsets.UTF_8)) {
            Locale locale = new LanguageSelector(scanner, output).readLanguage();

            assertEquals("ru", locale.getLanguage());
        }
    }

    @Test
    void invalidLanguageIsRejectedBeforeGermanSelection() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (Scanner scanner = new Scanner("en\nde\n");
             PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            Locale locale = new LanguageSelector(scanner, output).readLanguage();

            assertEquals(Locale.GERMAN, locale);
        }

        String result = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("Введите 'de' или 'ru'"));
    }
}
