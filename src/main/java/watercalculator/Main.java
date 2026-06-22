package watercalculator;

import watercalculator.application.WaterCalculatorService;
import watercalculator.config.SettingsLoader;
import watercalculator.domain.CalculatorSettings;
import watercalculator.ui.ConsoleApplication;
import watercalculator.ui.LanguageSelector;
import watercalculator.ui.Messages;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Locale locale = readLocaleOption(args);
            if (locale == null) {
                locale = new LanguageSelector(scanner, System.out).readLanguage();
            }
            Messages messages = new Messages(locale);

            try {
                CalculatorSettings settings = loadSettings(args);
                WaterCalculatorService calculator = new WaterCalculatorService(settings);

                new ConsoleApplication(scanner, System.out, calculator, settings, messages).run();
            } catch (IOException | IllegalArgumentException e) {
                System.err.println(messages.format("error.settings", e.getMessage()));
            }
        }
    }

    private static CalculatorSettings loadSettings(String[] args) throws IOException {
        SettingsLoader loader = new SettingsLoader();
        String configPath = findOption(args, "--config=");
        return configPath == null ? loader.loadDefault() : loader.load(Path.of(configPath));
    }

    private static Locale readLocaleOption(String[] args) {
        String language = findOption(args, "--lang=");
        if ("ru".equalsIgnoreCase(language)) {
            return Locale.forLanguageTag("ru");
        }
        if ("de".equalsIgnoreCase(language)) {
            return Locale.GERMAN;
        }
        return null;
    }

    private static String findOption(String[] args, String prefix) {
        for (String argument : args) {
            if (argument.startsWith(prefix)) {
                return argument.substring(prefix.length()).trim();
            }
        }
        return null;
    }
}
