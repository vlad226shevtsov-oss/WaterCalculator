package watercalculator.ui;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessagesTest {

    @Test
    void localizedTokensAreTrimmedAndCaseInsensitive() {
        Messages russian = new Messages(Locale.forLanguageTag("ru"));

        assertTrue(russian.matchesToken("choice.shower.tokens", " ДУШ "));
        assertTrue(russian.matchesToken("choice.yes.tokens", "Да"));
        assertTrue(russian.matchesToken("choice.currency.uah.tokens", "Гривна"));
        assertFalse(russian.matchesToken("choice.yes.tokens", "возможно"));
    }

    @Test
    void currencyUsesRequestedCurrency() {
        Messages german = new Messages(Locale.GERMAN);

        String formatted = german.formatCurrency(
                new BigDecimal("1.25"), Currency.getInstance("EUR"));

        assertTrue(formatted.contains("1,25"));
        assertTrue(formatted.contains("€"));
    }
}
