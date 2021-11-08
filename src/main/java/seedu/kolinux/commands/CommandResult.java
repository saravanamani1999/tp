package seedu.kolinux.commands;

/** Represents the result from the execution of a command. */
public class CommandResult {

    private String feedbackToUser;

    /**
     * Constructor for a CommandResult.
     *
     * @param feedbackToUser Feedback that is needed to be printed on the Ui, typically a success message
     *                       after executing a command.
     */
    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = feedbackToUser;
    }

    /**
     * Gets the feedback string.
     *
     * @return Feedback
     */
    public String getFeedbackToUser() {
        return feedbackToUser;
    }
}
