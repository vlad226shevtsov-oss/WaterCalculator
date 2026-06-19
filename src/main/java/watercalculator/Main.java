package watercalculator;

import watercalculator.application.WaterCalculatorService;
import watercalculator.config.SettingsLoader;
import watercalculator.domain.CalculatorSettings;
import watercalculator.ui.ConsoleApplication;
import watercalculator.ui.Messages;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Locale locale = readLocale(args);
        Messages messages = new Messages(locale);

        try {
            CalculatorSettings settings = loadSettings(args);
            WaterCalculatorService calculator = new WaterCalculatorService(settings);

            try (Scanner scanner = new Scanner(System.in)) {
                new ConsoleApplication(scanner, System.out, calculator, settings, messages).run();
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(messages.format("error.settings", e.getMessage()));
        }
    }

    private static CalculatorSettings loadSettings(String[] args) throws IOException {
        SettingsLoader loader = new SettingsLoader();
        String configPath = findOption(args, "--config=");
        return configPath == null ? loader.loadDefault() : loader.load(Path.of(configPath));
    }

    private static Locale readLocale(String[] args) {
        String language = findOption(args, "--lang=");
        return "ru".equalsIgnoreCase(language) ? Locale.forLanguageTag("ru") : Locale.GERMAN;
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
