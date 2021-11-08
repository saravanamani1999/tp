package seedu.kolinux.util;

import seedu.kolinux.commands.Command;
import seedu.kolinux.commands.HelpCommand;
import seedu.kolinux.commands.CalculateCapCommand;
import seedu.kolinux.commands.BusRouteCommand;
import seedu.kolinux.commands.ModuleCommand;
import seedu.kolinux.commands.InvalidCommand;
import seedu.kolinux.commands.PlannerCommand;
import seedu.kolinux.commands.TimetableCommand;
import seedu.kolinux.commands.ExitCommand;
import seedu.kolinux.exceptions.KolinuxException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Represents the operations to parse information needed for the execution of a command.
 */
public class Parser {

    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_CAP = "cap";
    private static final String COMMAND_BUS = "bus";
    private static final String COMMAND_MODULE = "module";
    private static final String COMMAND_PLANNER = "planner";
    private static final String COMMAND_EXIT = "bye";
    private static final String COMMAND_TIMETABLE = "timetable";

    private static final String EMPTY_STRING = "";
    // Use of pipe may corrupt the storage files
    private static final String ILLEGAL_CHAR = "|";
    private static final String SLASH_DELIMITER = "/";
    private static final String WHITE_SPACE = " ";

    private static final String DATE_PATTERN = "\\d\\d\\d\\d-\\d\\d-\\d\\d";
    private static final String TIME_PATTERN = "\\d\\d\\d\\d";

    private static final String ILLEGAL_CHAR_MESSAGE = "Please avoid using '|' in your input, please try again.";
    private static final String FATAL_ERROR = "Fatal error occurred, please restart Kolinux.";
    private static final String DATE_FORMAT_ERROR = "Please provide a valid date format. Format: yyyy-MM-dd";
    private static final String DATE_VALIDITY_ERROR = "This date does not exist. Please try again.";
    private static final String TIME_FORMAT_ERROR = "Please provide a valid time format. Format: HHmm";
    private static final String TIME_VALIDITY_ERROR = "This time is not valid. Please try again.";
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error occurred, please try again.";

    /**
     * Removes leading and trailing white spaces from all the elements in a String array.
     *
     * @param strings Array of strings
     * @return Array of strings with all elements trimmed
     */
    private static String[] trimAllElementsOfArray(String[] strings) {
        String[] trimmedStrings = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            trimmedStrings[i] = strings[i].trim();
        }
        return trimmedStrings;
    }

    /**
     * Gets the command word from the user input, and calls the respective Command subsequently for
     * execution.
     *
     * @param input User input
     * @return Command
     */
    public static Command parseCommand(String input) throws KolinuxException, IOException {
        if (input.contains(ILLEGAL_CHAR)) {
            throw new KolinuxException(ILLEGAL_CHAR_MESSAGE);
        }
        try {
            String trimmedInput = input.trim();
            String commandWord = trimmedInput.split(WHITE_SPACE, 2)[0];
            String argument = trimmedInput.replaceFirst(commandWord, EMPTY_STRING).trim();

            switch (commandWord.toLowerCase()) {
            case COMMAND_HELP:
                return new HelpCommand();
            case COMMAND_CAP:
                return parseCapCommand(argument);
            case COMMAND_BUS:
                return new BusRouteCommand(input);
            case COMMAND_MODULE:
                return parseSubCommand(argument, COMMAND_MODULE);
            case COMMAND_PLANNER:
                return parseSubCommand(argument, COMMAND_PLANNER);
            case COMMAND_EXIT:
                return new ExitCommand();
            case COMMAND_TIMETABLE:
                return parseSubCommand(argument, COMMAND_TIMETABLE);
            default:
                return new InvalidCommand();
            }
        } catch (PatternSyntaxException exception) {
            return new InvalidCommand();
        }
    }

    /**
     * Processes the arguments by separating the first word (sub-command) from the input.
     * The rest of the input is separated into a String array using the "/" delimiter.
     * This method should only be called if the commandWord is "timetable", "planner", or "module".
     *
     * @param subInput    User input without the command word
     * @param commandWord User commandWord
     * @return Command class according to commandWord
     */
    public static Command parseSubCommand(String subInput, String commandWord) throws KolinuxException {
        String subCommand = subInput.split(WHITE_SPACE, 2)[0];
        String argument = subInput.replaceFirst(subCommand, EMPTY_STRING).trim();
        String[] parsedArguments = trimAllElementsOfArray(argument.split(SLASH_DELIMITER));
        switch (commandWord) {
        case COMMAND_PLANNER:
            return new PlannerCommand(subCommand, parsedArguments);
        case COMMAND_TIMETABLE:
            return new TimetableCommand(subCommand, parsedArguments);
        case COMMAND_MODULE:
            return new ModuleCommand(subCommand, parsedArguments);
        default:
            throw new KolinuxException(INTERNAL_ERROR_MESSAGE);
        }
    }

    /**
     * Processes the arguments by separating the first word (sub-command) from the input.
     * The rest of the input is separated into a String array using the " " delimiter.
     * This method is called only when the "calculate cap" command is called.
     * 
     * @param subInput User input without the command word.
     * @return The "calculate cap" command.
     * @throws KolinuxException If the user's input contains invalid module descriptions.
     */
    public static Command parseCapCommand(String subInput) throws KolinuxException {
        String subCommand = subInput.split(WHITE_SPACE, 2)[0];
        String argument = subInput.replaceFirst(subCommand, EMPTY_STRING).trim();
        String[] parsedArguments = argument.split(WHITE_SPACE);
        return new CalculateCapCommand(subCommand, parsedArguments);
    }

    /**
     * Concatenates an array list of strings into a single string, starting with a newline and with newlines
     * separating consecutive entries.
     *
     * @param strings List of strings to be concatenated
     * @return Concatenated string of the list of strings
     */
    public static String concatenateStrings(ArrayList<String> strings) {
        String concatenatedString = EMPTY_STRING;
        for (String string : strings) {
            concatenatedString = concatenatedString.concat("\n" + string);
        }
        return concatenatedString;
    }

    /**
     * Returns the day of the week from a given date. The days of the week are represented
     * in integers 1 - 7 starting from Sunday.
     *
     * @param date Date specified
     * @return Day of the week represented by an integer
     * @throws ParseException If the date does not follow the format yyyy-MM-dd
     */
    public static int findDayFromDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    /**
     * Returns the day of the week in String, given an integer 1 - 7 starting from Sunday.
     *
     * @param day Integer representing the day
     * @return Day of the week in String represented by the integer
     */
    public static String parseDay(int day) {
        assert ((day >= 1) && (day <= 7));
        switch (day) {
        case 1:
            return "SUNDAY";
        case 2:
            return "MONDAY";
        case 3:
            return "TUESDAY";
        case 4:
            return "WEDNESDAY";
        case 5:
            return "THURSDAY";
        case 6:
            return "FRIDAY";
        case 7:
            return "SATURDAY";
        default:
            return "";
        }
    }

    /**
     * This method gets the day of the week in string given a specific date. It is important to conduct any relevant
     * checks for the format and validity of the date before this method is invoked.
     *
     * @param date Date
     * @return Day of the week in string
     */
    public static String getDayOfWeek(String date) {
        int dayInInt = 0;
        try {
            dayInInt = findDayFromDate(date);
        } catch (ParseException exception) {
            System.out.println(FATAL_ERROR);
            System.exit(0);
        }
        assert ((dayInInt >= 1) && (dayInInt <= 7));
        String day = parseDay(dayInInt);
        return day;
    }

    /**
     * Verifies if a given date is of the proper format (yyyy-mm-dd) and is valid.
     *
     * @param date Date to be verified
     * @return LocalDate object representing the date string
     * @throws KolinuxException If the format is incorrect or the date does not exist
     */
    public static LocalDate verifyDate(String date) throws KolinuxException {

        if (!Pattern.matches(DATE_PATTERN, date)) {
            throw new KolinuxException(DATE_FORMAT_ERROR);
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new KolinuxException(DATE_VALIDITY_ERROR);
        }

        return parsedDate;
    }

    /**
     * Verifies if a given time is of the proper format (hhMM) and is valid.
     *
     * @param time Time to be verified
     * @return LocalTime object representing the time string
     * @throws KolinuxException If the format is incorrect or the time is not valid
     */
    public static LocalTime verifyTime(String time) throws KolinuxException {

        if (!Pattern.matches(TIME_PATTERN, time)) {
            throw new KolinuxException(TIME_FORMAT_ERROR);
        }

        // Adds a colon between hh and MM for it to be parsed by the LocalTime class
        time = time.replaceFirst("..", "$0:");
        LocalTime parsedTime;
        try {
            parsedTime = LocalTime.parse(time);
        } catch (DateTimeParseException exception) {
            throw new KolinuxException(TIME_VALIDITY_ERROR);
        }

        return parsedTime;
    }
}
