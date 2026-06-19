package watercalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsumptionTypeTest {

    @Test
    void inputIsTrimmedAndCaseInsensitive() {
        assertEquals(ConsumptionType.SHOWER,
                ConsumptionType.fromInput(" Dusche ").orElseThrow());
        assertEquals(ConsumptionType.BATH,
                ConsumptionType.fromInput("BAD").orElseThrow());
    }

    @Test
    void unknownAndNullInputAreRejected() {
        assertTrue(ConsumptionType.fromInput("unknown").isEmpty());
        assertTrue(ConsumptionType.fromInput(null).isEmpty());
    }
}
