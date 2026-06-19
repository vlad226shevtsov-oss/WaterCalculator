package watercalculator.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Messages {

    private final Locale locale;
    private final ResourceBundle bundle;

    public Messages(Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public String text(String key) {
        return bundle.getString(key);
    }

    public String format(String key, Object... arguments) {
        MessageFormat formatter = new MessageFormat(text(key), locale);
        return formatter.format(arguments);
    }

    public boolean matchesToken(String key, String input) {
        if (input == null) {
            return false;
        }
        String normalizedInput = input.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(text(key).split(","))
                .map(String::trim)
                .map(token -> token.toLowerCase(Locale.ROOT))
                .anyMatch(normalizedInput::equals);
    }

    public String formatDecimal(BigDecimal value) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }

    public String formatCurrency(BigDecimal value, Currency currency) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setCurrency(currency);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }
}
