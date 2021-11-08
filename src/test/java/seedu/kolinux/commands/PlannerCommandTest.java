package seedu.kolinux.commands;

import org.junit.jupiter.api.Test;
import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.planner.Event;
import seedu.kolinux.planner.Planner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlannerCommandTest {

    // only used to clear events, as it is constructed without a valid ModuleList.
    private Planner planner = new Planner();

    private static final String ADD_SUBCOMMAND = "add";
    private static final String LIST_SUBCOMMAND = "list";
    private static final String DELETE_SUBCOMMAND = "delete";
    private static final String CLEAR_SUBCOMMAND = "clear";
    private static final String INVALID_SUBCOMMAND = "monkey business";

    private static final String[][] VALID_EVENT_ARGUMENTS
            = new String[][]{{"Pop Quiz 3", "2021-10-26", "1500", "1515"},
                {"Pop Quiz 4", "2021-10-26", "1530", "2000"},
                {"Pop Quiz 5", "2021-10-26", "2000", "2130"}};
    private static final String[] CONFLICTED_EVENT_ARGUMENTS
            = new String[]{"Conflict", "2021-10-26", "1510", "1520"};
    private static final String[] LIST_EVENT_ARGUMENTS = new String[]{"2021-10-26"};
    private static final String[][] INVALID_DATES = new String[][]{{"20211010"}, {"2021-02-29"}};

    private static final String ADD_EVENT_MESSAGE
            = "An event has been added to your schedule successfully: 2021-10-26 15:00 - 15:15 Pop Quiz 3";
    private static final String CONFLICT_EVENT_MESSAGE
            = "An event has been added to your schedule successfully: 2021-10-26 15:10 - 15:20 Conflict";
    private static final String DELETE_EVENT_MESSAGE
            = "An event has been deleted from your schedule successfully: 2021-10-26 15:30 - 20:00 Pop Quiz 4";
    private static final String INVALID_SUBCOMMAND_MESSAGE =
            "This command is not recognised, you can try:\n"
                    + "planner add DESCRIPTION/DATE/START_TIME/END_TIME\n"
                    + "planner list DATE\n"
                    + "planner delete DATE";
    private static final String CLEAR_EVENT_MESSAGE = "All the events in your schedule has been cleared.";

    private static final String LIST_EVENTS_TEST = "2021-10-26 TUESDAY\n"
            + "15:00 - 15:15 Pop Quiz 3\n"
            + "15:30 - 20:00 Pop Quiz 4\n"
            + "20:00 - 21:30 Pop Quiz 5";
    private static final String DELETE_EVENTS_TEST = "2021-10-26 TUESDAY\n"
            + "15:00 - 15:15 Pop Quiz 3\n"
            + "20:00 - 21:30 Pop Quiz 5";

    private static final String DATE_FORMAT_ERROR = "Please provide a valid date format. Format: yyyy-MM-dd";
    private static final String DATE_VALIDITY_ERROR = "This date does not exist. Please try again.";

    @Test
    public void handleAddCommand_validEvent_eventAdded() throws KolinuxException {
        planner.clearEvents();
        PlannerCommand command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[0]);
        CommandResult result = command.executeCommand();
        assertEquals(ADD_EVENT_MESSAGE, result.getFeedbackToUser());
        planner.clearEvents();
    }

    @Test
    public void handleAddCommand_conflictedEvent_controlHandedOverToPrompt() throws KolinuxException {
        planner.clearEvents();
        PlannerCommand command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[0]);
        command.executeCommand();

        String input = "y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        command = new PlannerCommand(ADD_SUBCOMMAND, CONFLICTED_EVENT_ARGUMENTS);
        CommandResult result = command.executeCommand();
        assertEquals(CONFLICT_EVENT_MESSAGE, result.getFeedbackToUser());
        planner.clearEvents();
    }

    @Test
    public void handleListCommand_addThreeEventsThenListThem_eventsListed() throws KolinuxException {
        planner.clearEvents();
        PlannerCommand command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[0]);
        command.executeCommand();
        command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[1]);
        command.executeCommand();
        command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[2]);
        command.executeCommand();
        command = new PlannerCommand(LIST_SUBCOMMAND, LIST_EVENT_ARGUMENTS);
        CommandResult result = command.executeCommand();
        assertEquals(LIST_EVENTS_TEST, result.getFeedbackToUser());
        planner.clearEvents();
    }

    @Test
    public void handleListCommand_wrongDateFormat_exceptionThrown() {
        PlannerCommand command = new PlannerCommand(LIST_SUBCOMMAND, INVALID_DATES[0]);
        try {
            command.executeCommand();
        } catch (KolinuxException exception) {
            assertEquals(DATE_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void handleListCommand_invalidDate_exceptionThrown() {
        PlannerCommand command = new PlannerCommand(LIST_SUBCOMMAND, INVALID_DATES[1]);
        try {
            command.executeCommand();
        } catch (KolinuxException exception) {
            assertEquals(DATE_VALIDITY_ERROR, exception.getMessage());
        }
    }

    @Test
    public void handleDeleteCommand_validIdGiven_eventDeleted() throws KolinuxException {
        planner.clearEvents();
        PlannerCommand command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[0]);
        command.executeCommand();
        final Event placeholder = new Event(VALID_EVENT_ARGUMENTS[1]);
        command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[1]);
        command.executeCommand();
        command = new PlannerCommand(ADD_SUBCOMMAND, VALID_EVENT_ARGUMENTS[2]);
        command.executeCommand();

        // event to be deleted is created immediately after the placeholder
        String input = Integer.toString(Integer.parseInt(placeholder.getId()) + 1);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        command = new PlannerCommand(DELETE_SUBCOMMAND, LIST_EVENT_ARGUMENTS);
        CommandResult result = command.executeCommand();
        assertEquals(DELETE_EVENT_MESSAGE, result.getFeedbackToUser());

        command = new PlannerCommand(LIST_SUBCOMMAND, LIST_EVENT_ARGUMENTS);
        result = command.executeCommand();
        assertEquals(DELETE_EVENTS_TEST, result.getFeedbackToUser());
        planner.clearEvents();
    }

    @Test
    public void handleDeleteCommand_wrongDateFormat_exceptionThrown() {
        PlannerCommand command = new PlannerCommand(DELETE_SUBCOMMAND, INVALID_DATES[0]);
        try {
            command.executeCommand();
        } catch (KolinuxException exception) {
            assertEquals(DATE_FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    public void handleDeleteCommand_invalidDate_exceptionThrown() {
        PlannerCommand command = new PlannerCommand(DELETE_SUBCOMMAND, INVALID_DATES[1]);
        try {
            command.executeCommand();
        } catch (KolinuxException exception) {
            assertEquals(DATE_VALIDITY_ERROR, exception.getMessage());
        }
    }

    @Test
    public void handleInvalidCommand_invalidSubCommand_exceptionThrown() {
        PlannerCommand command = new PlannerCommand(INVALID_SUBCOMMAND, null);
        try {
            command.executeCommand();
        } catch (KolinuxException exception) {
            assertEquals(INVALID_SUBCOMMAND_MESSAGE, exception.getMessage());
        }
    }

    @Test
    public void handleClearCommand_noArguments_eventsCleared() throws KolinuxException {
        PlannerCommand command = new PlannerCommand(CLEAR_SUBCOMMAND, null);
        CommandResult result = command.executeCommand();
        assertEquals(CLEAR_EVENT_MESSAGE, result.getFeedbackToUser());
    }
}
