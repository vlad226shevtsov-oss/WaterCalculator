package watercalculator;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final int LOW_CONSUMPTION_LIMIT = 60;
    private static final int NORMAL_CONSUMPTION_LIMIT = 100;
    private static final int HIGH_CONSUMPTION_LIMIT = 150;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            run(scanner);
        }
    }

    static void run(Scanner scanner) {
        ConsumptionType type = readConsumptionType(scanner);
        int showerMinutes = type == ConsumptionType.SHOWER
                ? readPositiveInt(scanner, "Wie viele Minuten duschst du pro Tag? ")
                : 0;

        WaterReport report = WaterCalculator.calculateReport(type, showerMinutes);
        printReport(report);
        askAboutDishwashing(scanner);
    }

    private static ConsumptionType readConsumptionType(Scanner scanner) {
        while (true) {
            System.out.print("Dusche oder Bad? (dusche/bad): ");
            String input = scanner.nextLine();
            var type = ConsumptionType.fromInput(input);

            if (type.isPresent()) {
                return type.get();
            }
            System.out.println("Bitte gib 'dusche' oder 'bad' ein.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Bitte gib eine Zahl größer als 0 ein.");
            } catch (NumberFormatException e) {
                System.out.println("Das ist keine Zahl. Bitte versuche es erneut.");
            }
        }
    }

    private static void printReport(WaterReport report) {
        System.out.println("\n--- Dein Wasserbericht ---\n");
        System.out.println("Art: " + report.type().inputValue());
        System.out.println("Wasserverbrauch: " + report.liters() + " Liter");
        System.out.printf("Energieverbrauch: %.2f kWh%n%n", report.energyKwh());

        System.out.println("Bewertung:");
        if (report.type() == ConsumptionType.SHOWER) {
            System.out.println("Du verbrauchst ca. " + report.liters() + " Liter Wasser pro Dusche.");
        } else {
            System.out.println("Du verbrauchst ca. " + report.liters() + " Liter Wasser pro Bad.");
        }

        printAssessment(report);
        printSavingTip(report);
    }

    private static void printAssessment(WaterReport report) {
        int water = report.liters();
        if (water <= LOW_CONSUMPTION_LIMIT) {
            System.out.println("Sehr gut! Dein Wasserverbrauch ist niedrig.");
        } else if (water <= NORMAL_CONSUMPTION_LIMIT) {
            System.out.println("Dein Wasserverbrauch ist normal.");
        } else if (water <= HIGH_CONSUMPTION_LIMIT) {
            if (report.type() == ConsumptionType.SHOWER) {
                System.out.println("Du verbrauchst viel Wasser. Vielleicht kürzer duschen?");
            } else {
                System.out.println("Du verbrauchst viel Wasser. Ein Bad verbraucht viel Wasser.");
            }
        } else {
            System.out.println("Sehr hoher Wasserverbrauch. Bitte spare Wasser!");
        }
    }

    private static void printSavingTip(WaterReport report) {
        if (report.possibleSavingLiters() > 0) {
            System.out.println("Wenn du nur " + WaterCalculator.TARGET_SHOWER_MINUTES
                    + " Minuten duschst, kannst du " + report.possibleSavingLiters()
                    + " Liter Wasser sparen.");
        }
    }

    private static void askAboutDishwashing(Scanner scanner) {
        while (true) {
            System.out.print(
                    "Möchtest du wissen, wie viel Wasser fürs Geschirrspülen verbraucht wird? (ja/nein): "
            );
            String choice = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            if (choice.equals("ja")) {
                System.out.println("Beim Geschirrspülen werden im Durchschnitt ca. "
                        + WaterCalculator.DISHWASHER_LITERS + " Liter Wasser verbraucht.");
                return;
            }
            if (choice.equals("nein")) {
                System.out.println("Alles klar. Tschüss!");
                return;
            }
            System.out.println("Bitte gib nur 'ja' oder 'nein' ein.");
        }
    }
}
