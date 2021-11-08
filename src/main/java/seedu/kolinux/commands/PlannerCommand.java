package seedu.kolinux.commands;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.planner.Event;
import seedu.kolinux.planner.Planner;
import seedu.kolinux.planner.PlannerPromptHandler;
import seedu.kolinux.util.Parser;

import java.util.logging.Level;

/** Represents the command that interacts with the Planner. */
public class PlannerCommand extends Command {

    private Planner planner = new Planner(moduleList);

    private String subCommand;
    private String[] parsedArguments;

    private static final String WHITE_SPACE = " ";

    // subcommands of planner
    private static final String ADD_SUBCOMMAND = "add";
    private static final String LIST_SUBCOMMAND = "list";
    private static final String DELETE_SUBCOMMAND = "delete";
    private static final String CLEAR_SUBCOMMAND = "clear";

    // feedback messages upon successful execution of subcommands
    private static final String ADD_EVENT_MESSAGE = "An event has been added to your schedule successfully: ";
    private static final String DELETE_EVENT_MESSAGE = "An event has been deleted from your schedule successfully: ";
    private static final String CLEAR_EVENT_MESSAGE = "All the events in your schedule has been cleared.";

    // prompts
    private static final String TIME_CONFLICT_PROMPT =
            "You already have an event ongoing for that time period, do you still want to add?\n"
                    + "You may enter 'n' to cancel and proceed to list the events on the date to see what you already "
                    + "planned on that day\n"
                    + "Or you may enter 'y' to add the event";
    private static final String ENTER_ID_PROMPT =
            "Please enter the ID of the event you wish to delete (Enter 'n' to terminate this operation):";

    // when invalid subcommand is recognised
    private static final String INVALID_ARGUMENT_MESSAGE =
            "This command is not recognised, you can try:\n"
                    + "planner add DESCRIPTION/DATE/START_TIME/END_TIME\n"
                    + "planner list DATE\n"
                    + "planner delete DATE";

    /**
     * Constructor for PlannerCommand, which is invoked when the first word of the user input is "planner".
     *
     * @param subCommand Sub-command of the planner, either add, list, or delete.
     * @param parsedArguments Parameters that follows the sub-command, which is separated into an array using the "/"
     *                        delimiter.
     */
    public PlannerCommand(String subCommand, String[] parsedArguments) {
        this.subCommand = subCommand;
        this.parsedArguments = parsedArguments;
    }

    /**
     * Invoked if the subcommand is "add". This method tries to add the event, and if a time conflict
     * occurs, it will hand over the control to PlannerPromptHandler. If the user approves the conflict,
     * the event will be added, else if the user denies, an exception is thrown. If the user does not
     * provide a valid reply, the prompt will continue asking for permission.
     *
     * @return Result containing success message
     * @throws KolinuxException If the event cannot be created due to incorrect arguments, or the user
     *     cancels the operation due to conflict.
     */
    private CommandResult handleAddCommand() throws KolinuxException {
        Event event = new Event(parsedArguments);
        try {
            planner.addEvent(event, false);
            logger.log(Level.INFO, "User added an event to planner: " + event);
        } catch (KolinuxException exception) {
            // in case of time conflict
            assert exception.getMessage().equals(TIME_CONFLICT_PROMPT);
            new PlannerPromptHandler(planner, TIME_CONFLICT_PROMPT).handleEventConflict(event);
        }
        return new CommandResult(ADD_EVENT_MESSAGE + event.getDate() + WHITE_SPACE + event);
    }

    /**
     * Invoked if the subcommand is "list". This method lists the events happening on a particular date.
     *
     * @return Result containing the list of events
     * @throws KolinuxException If the arguments are invalid or there are no events happening on the date
     */
    private CommandResult handleListCommand() throws KolinuxException {
        Parser.verifyDate(parsedArguments[0]);
        String dayOfWeek = Parser.getDayOfWeek(parsedArguments[0]);
        String eventList = planner.listEvents(parsedArguments[0], false);
        logger.log(Level.INFO, "User listed events on " + parsedArguments[0]);
        return new CommandResult(parsedArguments[0] + WHITE_SPACE + dayOfWeek + eventList);
    }

    /**
     * Invoked if the subcommand is "delete". This method deletes an event on a particular date. It starts by
     * displaying the list of events with their id to the user, prompting the user to key in the id of the event
     * to be deleted. If the id is not recognised, an exception is thrown.
     *
     * @return Result containing the message
     * @throws KolinuxException If the id is not recognised or the user cancels the operation
     */
    private CommandResult handleDeleteCommand() throws KolinuxException {
        Parser.verifyDate(parsedArguments[0]);
        String idList = planner.listEvents(parsedArguments[0], true);
        String id = new PlannerPromptHandler(planner, ENTER_ID_PROMPT + idList).promptForEventId();
        Event deletedEvent = planner.deleteEvent(id);
        logger.log(Level.INFO, "User deleted an event: " + deletedEvent);
        return new CommandResult(DELETE_EVENT_MESSAGE + deletedEvent.getDate() + WHITE_SPACE
                + deletedEvent);
    }

    /**
     * Clears all events stored in scheduleOfAllDates in Planner. This is only used when the subcommand is "clear",
     * which is not known to the user. This is only used for convenience while developing this feature.
     *
     * @return Result stating all events have been cleared.
     */
    private CommandResult handleClearCommand() {
        planner.clearEvents();
        return new CommandResult(CLEAR_EVENT_MESSAGE);
    }

    @Override
    public CommandResult executeCommand() throws KolinuxException {
        switch (subCommand) {
        case ADD_SUBCOMMAND:
            return handleAddCommand();
        case LIST_SUBCOMMAND:
            return handleListCommand();
        case DELETE_SUBCOMMAND:
            return handleDeleteCommand();
        case CLEAR_SUBCOMMAND:
            // Command only for testing purposes, not known to the user.
            return handleClearCommand();
        default:
            logger.log(Level.INFO, "User entered an invalid sub-command of Planner");
            throw new KolinuxException(INVALID_ARGUMENT_MESSAGE);
        }
    }
}
