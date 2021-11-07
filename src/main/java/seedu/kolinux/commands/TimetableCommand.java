package seedu.kolinux.commands;

import seedu.kolinux.exceptions.ExceedWorkloadException;
import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.timetable.Timetable;
import seedu.kolinux.timetable.TimetablePromptHandler;

import java.util.logging.Level;

/** Represents the command that interacts with the timetable. */
public class TimetableCommand extends Command {

    public static Timetable timetable = new Timetable(moduleList);
    private String subCommand;
    private String[] parsedArguments;
    public static final String INVALID_TIMETABLE_ARGUMENT_MESSAGE = "Ensure command has one of the "
            +
            "following formats:\n1. timetable add LESSON_TYPE/MODULE_CODE/DAY/START_TIME\n"
            +
            "2. timetable view\n"
            +
            "3. timetable update MODULE_CODE/LESSON_TYPE/OLD_DAY/OLD_START_TIME/NEW_DAY/NEW_START_TIME\n"
            +
            "4. timetable delete MODULE_CODE/LESSON_TYPE/DAY/START_TIME\n"
            +
            "5. timetable list DAY";

    private static final String ADD_SUBCOMMAND = "add";
    private static final String CLEAR_SUBCOMMAND = "clear";
    private static final String UPDATE_SUBCOMMAND = "update";
    private static final String DELETE_SUBCOMMAND = "delete";
    private static final String VIEW_SUBCOMMAND = "view";
    private static final String LIST_SUBCOMMAND = "list";

    public TimetableCommand(String subCommand, String[] parsedArguments) {
        this.subCommand = subCommand;
        this.parsedArguments = parsedArguments;
    }

    /**
     * Carries out the operation of adding to the timetable.
     *
     * @return The acknowledgment message after adding to timetable
     * @throws KolinuxException If the lesson details are invalid
     */
    private CommandResult addLesson() throws KolinuxException {
        try {
            timetable.executeAdd(parsedArguments, false);
        } catch (ExceedWorkloadException exception) {
            new TimetablePromptHandler(exception.getMessage(), timetable).handleExceedWorkload(parsedArguments);
        }
        logger.log(Level.INFO, "User added a module to timetable");
        String moduleCode = parsedArguments[0].toUpperCase();
        String lessonType = parsedArguments[1].toUpperCase();
        return new CommandResult(moduleCode + " " + lessonType + " has been added to timetable");
    }

    /**
     * Carries out the operation of deleting from the timetable.
     *
     * @return The acknowledgment message after deleting from timetable
     * @throws KolinuxException If the lesson details are invalid
     */
    private CommandResult deleteLesson() throws KolinuxException {
        timetable.executeDelete(parsedArguments);
        String moduleCode = parsedArguments[0].toUpperCase();
        String lessonType = parsedArguments[1].toUpperCase();
        String day = parsedArguments[2].toLowerCase();
        String startTime = parsedArguments[3];
        logger.log(Level.INFO, "User has deleted" + moduleCode + " from the timetable.");
        return new CommandResult(moduleCode + " " + lessonType + " " + startTime
                + " " + day + " has been deleted from timetable");
    }

    /**
     * Carries out the operation of viewing the timetable on CLI.
     *
     * @return The acknowledgment message after printing the timetable to CLI
     */
    private CommandResult viewTimetable() {
        timetable.executeView();
        logger.log(Level.INFO, "User has printed timetable");
        return new CommandResult("Timetable has been printed above");
    }

    /**
     * Carries out the operation of clearing all lessons from the timetable.
     *
     * @return The acknowledgment message after clearing all lessons from the timetable
     */
    private CommandResult clearAllLessons() {
        timetable.clearTimetable();
        logger.log(Level.INFO, "User has cleared timetable");
        return new CommandResult("Timetable has been cleared completely");
    }

    /**
     * Carries out the operation of updating a lesson's timing on the timetable.
     *
     * @return The acknowledgment message after updating the timetable
     * @throws KolinuxException If the lesson details to update are invalid
     */
    private CommandResult updateLesson() throws KolinuxException {
        timetable.executeUpdate(parsedArguments);
        String moduleCode = parsedArguments[0].toUpperCase();
        String lessonType = parsedArguments[1].toUpperCase();
        logger.log(Level.INFO, "User has updated the timetable.");
        return new CommandResult(moduleCode + " " + lessonType + " has been updated");
    }

    /**
     * Carries out the operation of listing the lessons on a specific weekday.
     *
     * @return The acknowledgment message after listing the lessons on a specific day
     * @throws KolinuxException If the specified day is invalid
     */
    private CommandResult listLesson() throws KolinuxException {
        try {
            String day = parsedArguments[0].toLowerCase();
            timetable.listTimetable(day);
            logger.log(Level.INFO, "User has listed the timetable.");
            return new CommandResult("\nYour lessons for " + day + " has been listed above");
        } catch (IndexOutOfBoundsException exception) {
            throw new KolinuxException("Please ensure the format of timetable list:\ntimetable list DAY");
        }
    }

    @Override
    public CommandResult executeCommand() throws KolinuxException {
        switch (subCommand) {
        case ADD_SUBCOMMAND:
            return addLesson();
        case CLEAR_SUBCOMMAND:
            return clearAllLessons();
        case VIEW_SUBCOMMAND:
            return viewTimetable();
        case DELETE_SUBCOMMAND:
            return deleteLesson();
        case UPDATE_SUBCOMMAND:
            return updateLesson();
        case LIST_SUBCOMMAND:
            return listLesson();
        default:
            logger.log(Level.INFO, "User used invalid subCommand for timetable");
            return new CommandResult(INVALID_TIMETABLE_ARGUMENT_MESSAGE);
        }
    }

}
