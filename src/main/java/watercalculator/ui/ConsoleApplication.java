package watercalculator.ui;

import watercalculator.application.WaterCalculatorService;
import watercalculator.domain.Bath;
import watercalculator.domain.CalculatorSettings;
import watercalculator.domain.ConsumptionType;
import watercalculator.domain.Shower;
import watercalculator.domain.WaterConsumption;
import watercalculator.domain.WaterReport;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public final class ConsoleApplication {

    private final Scanner scanner;
    private final PrintStream output;
    private final WaterCalculatorService calculator;
    private final CalculatorSettings settings;
    private final Messages messages;

    public ConsoleApplication(
            Scanner scanner,
            PrintStream output,
            WaterCalculatorService calculator,
            CalculatorSettings settings,
            Messages messages
    ) {
        this.scanner = Objects.requireNonNull(scanner, "scanner must not be null");
        this.output = Objects.requireNonNull(output, "output must not be null");
        this.calculator = Objects.requireNonNull(calculator, "calculator must not be null");
        this.settings = Objects.requireNonNull(settings, "settings must not be null");
        this.messages = Objects.requireNonNull(messages, "messages must not be null");
    }

    public void run() {
        WaterConsumption consumption = readConsumption();
        WaterReport report = calculator.calculate(consumption);
        printReport(report);
        askAboutDishwashing();
    }

    private WaterConsumption readConsumption() {
        while (true) {
            output.print(messages.text("prompt.type") + " ");
            String input = scanner.nextLine();

            if (messages.matchesToken("choice.shower.tokens", input)) {
                return new Shower(readPositiveInt(messages.text("prompt.shower-minutes")));
            }
            if (messages.matchesToken("choice.bath.tokens", input)) {
                return new Bath();
            }
            output.println(messages.text("error.type"));
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            output.print(prompt + " ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                output.println(messages.text("error.positive-number"));
            } catch (NumberFormatException e) {
                output.println(messages.text("error.number"));
            }
        }
    }

    private void printReport(WaterReport report) {
        output.println();
        output.println(messages.text("report.title"));
        output.println();
        output.println(messages.format("report.type", typeName(report.type())));
        output.println(messages.format("report.water", report.liters()));
        output.println(messages.format("report.energy", messages.formatDecimal(report.energyKwh())));
        output.println(messages.format("report.water-cost",
                messages.formatCurrency(report.waterCost(), settings.currency())));
        output.println(messages.format("report.energy-cost",
                messages.formatCurrency(report.energyCost(), settings.currency())));
        output.println(messages.format("report.total-cost",
                messages.formatCurrency(report.totalCost(), settings.currency())));
        output.println();
        output.println(messages.text("report.rating-title"));
        output.println(messages.text(ratingKey(report)));

        if (report.possibleSavingLiters() > 0) {
            output.println(messages.format(
                    "report.saving",
                    settings.targetShowerMinutes(),
                    report.possibleSavingLiters()
            ));
        }
    }

    private String typeName(ConsumptionType type) {
        return messages.text(type == ConsumptionType.SHOWER ? "type.shower" : "type.bath");
    }

    private String ratingKey(WaterReport report) {
        return switch (report.rating()) {
            case LOW -> "rating.low";
            case NORMAL -> "rating.normal";
            case HIGH -> report.type() == ConsumptionType.SHOWER
                    ? "rating.high.shower"
                    : "rating.high.bath";
            case VERY_HIGH -> "rating.very-high";
        };
    }

    private void askAboutDishwashing() {
        while (true) {
            output.print(messages.text("prompt.dishwasher") + " ");
            String choice = scanner.nextLine();

            if (messages.matchesToken("choice.yes.tokens", choice)) {
                output.println(messages.format("dishwasher.result", settings.dishwasherLiters()));
                return;
            }
            if (messages.matchesToken("choice.no.tokens", choice)) {
                output.println(messages.text("goodbye"));
                return;
            }
            output.println(messages.text("error.yes-no"));
        }
    }
}
