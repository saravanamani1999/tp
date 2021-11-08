package seedu.kolinux.planner;

import org.junit.jupiter.api.Test;
import seedu.kolinux.exceptions.KolinuxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    private static final String EVENT_DATA_STRING = "Some data | 2021-10-25|1200| 1500";
    private static final String CORRUPTED_DATA_STRING = "some corrupted data||0x|[]*";
    private static final String EVENT_FROM_DATA = "12:00 - 15:00 Some data";

    private static final String DATE_FORMAT_ERROR = "Please provide a valid date format. Format: yyyy-MM-dd";
    private static final String DATE_VALIDITY_ERROR = "This date does not exist. Please try again.";
    private static final String TIME_FORMAT_ERROR = "Please provide a valid time format. Format: HHmm";
    private static final String TIME_VALIDITY_ERROR = "This time is not valid. Please try again.";

    private static final String[][] INVALID_EVENT_ARGUMENTS
            = new String[][]{{"wrong date format", "20211010", "1900", "2100"},
                {"invalid date", "2021-02-29", "1900", "2100"},
                {"wrong time format", "2021-10-10", "7pm", "2200"},
                {"invalid time", "2021-10-10", "1900", "1969"}};

    @Test
    public void constructEvent_eventDataString_eventConstructed() throws KolinuxException {
        Event event = new Event(EVENT_DATA_STRING);
        assertEquals(EVENT_FROM_DATA, event.toString());
        assertEquals("2021-10-25", event.getDate());
    }

    @Test
    public void constructEvent_corruptedEventDataString_exceptionThrown() {
        try {
            new Event(CORRUPTED_DATA_STRING);
        } catch (KolinuxException exception) {
            assertEquals(DATE_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void constructEvent_wrongDateFormat_exceptionThrown() {
        try {
            new Event(INVALID_EVENT_ARGUMENTS[0]);
        } catch (KolinuxException exception) {
            assertEquals(DATE_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void constructEvent_invalidDate_exceptionThrown() {
        try {
            new Event(INVALID_EVENT_ARGUMENTS[1]);
        } catch (KolinuxException exception) {
            assertEquals(DATE_VALIDITY_ERROR, exception.getMessage());
        }
    }

    @Test
    public void constructEvent_wrongTimeFormat_exceptionThrown() {
        try {
            new Event(INVALID_EVENT_ARGUMENTS[2]);
        } catch (KolinuxException exception) {
            assertEquals(TIME_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void constructEvent_invalidTime_exceptionThrown() {
        try {
            new Event(INVALID_EVENT_ARGUMENTS[3]);
        } catch (KolinuxException exception) {
            assertEquals(TIME_VALIDITY_ERROR, exception.getMessage());
        }
    }
}
