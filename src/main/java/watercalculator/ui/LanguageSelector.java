package watercalculator.ui;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public final class LanguageSelector {

    private final Scanner scanner;
    private final PrintStream output;

    public LanguageSelector(Scanner scanner, PrintStream output) {
        this.scanner = Objects.requireNonNull(scanner, "scanner must not be null");
        this.output = Objects.requireNonNull(output, "output must not be null");
    }

    public Locale readLanguage() {
        while (true) {
            output.print("Sprache wählen / Выберите язык (de/ru): ");
            String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            if (input.equals("de")) {
                return Locale.GERMAN;
            }
            if (input.equals("ru")) {
                return Locale.forLanguageTag("ru");
            }
            output.println("Bitte 'de' oder 'ru' eingeben. / Введите 'de' или 'ru'.");
        }
    }
}
