package seedu.kolinux.planner;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.util.PromptHandler;

import java.util.logging.Level;

/** Represents the operations related to prompting the user while using the Planner feature. */
public class PlannerPromptHandler extends PromptHandler {

    private Planner planner;

    private static final String YES = "y";
    private static final String NO = "n";

    private static final String CANCEL_ADD_ERROR = "Operation cancelled, no events were added.";
    private static final String INVALID_REPLY_ERROR = "Invalid key entered! Please enter 'y' or 'n'.";
    private static final String CANCEL_DELETE_ERROR = "Operation cancelled, no events were deleted.";

    /**
     * Constructor for this class, which is used when the user needs to delete an event, or faces a time conflict
     * when trying to add events.
     *
     * @param planner Planner
     * @param message Prompt message for the user to reply to
     */
    public PlannerPromptHandler(Planner planner, String message) {
        super(message);
        this.planner = planner;
    }

    /**
     * Infinite loop of posting prompt to the user to handle time conflicts in events when adding.
     * The loop only exits if the user provides a valid answer 'y' or 'n'.
     *
     * @param event Conflicted event
     * @throws KolinuxException If the user cancels the add operation
     */
    public void handleEventConflict(Event event) throws KolinuxException {
        while (true) {
            String reply = getReplyFromPrompt();
            if (reply.equalsIgnoreCase(YES)) {
                planner.addEvent(event, true);
                logger.log(Level.INFO, "User added an event to planner with conflict: " + event);
                break;
            } else if (reply.equalsIgnoreCase(NO)) {
                logger.log(Level.INFO, "User cancelled the planner add operation.");
                throw new KolinuxException(CANCEL_ADD_ERROR);
            } else {
                this.changeMessage(INVALID_REPLY_ERROR);
            }
        }
    }

    /**
     * Posts a prompt to ask for the ID of the event to be deleted.
     *
     * @return ID of the event
     * @throws KolinuxException If the user enters 'n' to cancel the delete operation
     */
    public String promptForEventId() throws KolinuxException {
        String id = getReplyFromPrompt();
        if (id.equalsIgnoreCase(NO)) {
            logger.log(Level.INFO, "User cancelled the planner delete operation.");
            throw new KolinuxException(CANCEL_DELETE_ERROR);
        }
        return id;
    }
}
