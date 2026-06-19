package watercalculator;

import java.util.Locale;
import java.util.Optional;

public enum ConsumptionType {
    SHOWER("dusche"),
    BATH("bad");

    private final String inputValue;

    ConsumptionType(String inputValue) {
        this.inputValue = inputValue;
    }

    public String inputValue() {
        return inputValue;
    }

    public static Optional<ConsumptionType> fromInput(String input) {
        if (input == null) {
            return Optional.empty();
        }

        String normalizedInput = input.trim().toLowerCase(Locale.ROOT);
        for (ConsumptionType type : values()) {
            if (type.inputValue.equals(normalizedInput)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
