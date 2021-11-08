package seedu.kolinux.commands;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDb;
import seedu.kolinux.module.ModuleList;

import java.io.IOException;
import java.util.logging.Logger;

/** Parent class of all commands available on Kolinux. This also contains class-level attributes such as
 * Logger, ModuleDb, and ModuleList, which are used by child classes. */
public abstract class Command {

    protected String argument;
    protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    protected static ModuleDb moduleDb = new ModuleDb().getPreInitModuleDb();
    protected static ModuleList moduleList = new ModuleList();

    /**
     * Constructor for commands which do not require any parameters.
     */
    public Command() {

    }

    /**
     * Constructor for commands which require additional information to be sent in as parameters.
     *
     * @param argument Parameter for the command
     */
    public Command(String argument) {
        this.argument = argument;
    }

    /**
     * Executes the command with the arguments given by the user input.
     *
     * @return Result of execution
     * @throws KolinuxException If the arguments given are invalid
     * @throws IOException If there are errors accessing the resource file
     */
    public abstract CommandResult executeCommand() throws KolinuxException, IOException;
}
