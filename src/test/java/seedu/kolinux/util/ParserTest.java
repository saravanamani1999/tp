package seedu.kolinux.util;

import org.junit.jupiter.api.Test;
import seedu.kolinux.exceptions.KolinuxException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    private static final String DATE_FORMAT_ERROR = "Please provide a valid date format. Format: yyyy-MM-dd";
    private static final String DATE_VALIDITY_ERROR = "This date does not exist. Please try again.";
    private static final String TIME_FORMAT_ERROR = "Please provide a valid time format. Format: HHmm";
    private static final String TIME_VALIDITY_ERROR = "This time is not valid. Please try again.";

    @Test
    public void findDayFromDate_validDates_daysReturned() throws ParseException {
        int day = Parser.findDayFromDate("2021-10-25");
        assertEquals(2, day);
        day = Parser.findDayFromDate("2021-10-26");
        assertEquals(3, day);
        day = Parser.findDayFromDate("2021-10-27");
        assertEquals(4, day);
        day = Parser.findDayFromDate("2021-10-28");
        assertEquals(5, day);
        day = Parser.findDayFromDate("2021-10-29");
        assertEquals(6, day);
        day = Parser.findDayFromDate("2021-10-30");
        assertEquals(7, day);
        day = Parser.findDayFromDate("2021-10-31");
        assertEquals(1, day);
    }

    @Test
    public void findDayFromDate_invalidDate_exceptionThrown() {
        assertThrows(ParseException.class, () -> Parser.findDayFromDate("21 January 2021"));
    }

    @Test
    public void verifyDate_correctDate_localDateReturned() throws KolinuxException {
        LocalDate date = Parser.verifyDate("2021-10-10");
        assertEquals("2021-10-10", date.toString());
    }

    @Test
    public void verifyDate_wrongDateFormat_exceptionThrown() {
        try {
            Parser.verifyDate("20211010");
        } catch (KolinuxException exception) {
            assertEquals(DATE_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void verifyDate_invalidDate_exceptionThrown() {
        try {
            Parser.verifyDate("2021-12-32");
        } catch (KolinuxException exception) {
            assertEquals(DATE_VALIDITY_ERROR, exception.getMessage());
        }
    }

    @Test
    public void verifyTime_correctTime_localTimeReturned() throws KolinuxException {
        LocalTime time = Parser.verifyTime("2200");
        assertEquals("22:00", time.toString());
    }

    @Test
    public void verifyTime_wrongTimeFormat_exceptionThrown() {
        try {
            Parser.verifyTime("10pm");
        } catch (KolinuxException exception) {
            assertEquals(TIME_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void verifyTime_invalidTime_exceptionThrown() {
        try {
            Parser.verifyTime("1970");
        } catch (KolinuxException exception) {
            assertEquals(TIME_VALIDITY_ERROR, exception.getMessage());
        }
    }
}
