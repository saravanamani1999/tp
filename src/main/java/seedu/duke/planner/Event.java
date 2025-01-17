package seedu.duke.planner;

import seedu.duke.exceptions.KolinuxException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/** Represents an event in the schedule. */
public class Event {

    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private static final String DATETIME_ERROR =
            "Please provide a valid date and time! Format: yyyy-mm-dd";
    private static final String FORMAT_ERROR =
            "Please check the format of your input! Format: planner add DESCRIPTION/DATE/START_TIME/END_TIME";

    /**
     * Constructs an event using the arguments given in the user input.
     *
     * @param parsedArguments Array of information of an event
     * @throws KolinuxException If any of the information required is missing or in incorrect format.
     */
    public Event(String[] parsedArguments) throws KolinuxException {
        try {
            this.description = parsedArguments[0];
            this.date = LocalDate.parse(parsedArguments[1]);
            this.startTime = LocalTime.parse(parsedArguments[2]);
            this.endTime = LocalTime.parse(parsedArguments[3]);
        } catch (DateTimeParseException e) {
            throw new KolinuxException(DATETIME_ERROR);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new KolinuxException(FORMAT_ERROR);
        }
    }

    /**
     * Constructs an event using the data read from planner.txt. This constructor is only called to
     * add previously saved events in planner.txt to the current active list.
     *
     * @param data Data line read from the file
     * @throws KolinuxException If the data line is corrupted
     */
    public Event(String data) throws KolinuxException {
        this(data.split("\\|"));
    }

    public String getDate() {
        return date.toString();
    }

    /**
     * Converts the event to a data string that is stored in planner.txt.
     * Note: This string is different from the one displayed to the user on the user interface.
     *
     * @return Data string
     */
    public String toData() {
        return description + "|" + date.toString() + "|" + startTime.toString() + "|" + endTime.toString();
    }

    public String toString() {
        return startTime + " - " + endTime + " " + description;
    }
}
