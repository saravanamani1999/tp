package seedu.kolinux.commands;

import seedu.kolinux.capcalculator.CapCalculator;
import seedu.kolinux.capcalculator.CapCalculatorByCode;
import seedu.kolinux.capcalculator.CapCalculatorByMc;
import seedu.kolinux.exceptions.KolinuxException;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Represents the command that calculate CAP from user input.
 */
public class CalculateCapCommand extends Command {
    
    //store calculator
    private CapCalculator calculator;
    
    private boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
    
    private String getModuleInfoFormat(String moduleInfo) {
        if (isNumeric(moduleInfo.substring(0, 1))) {
            return "mc";
        } else {
            return "code";
        }
    }
    
    /**
     * Constructs this object and initializes array to store module information.
     * 
     * @param input Command input from user which contains modular credits and grades.
     */
    public CalculateCapCommand(String input) {
        String[] commandDescriptions = input.split(" ");
        String moduleInfoFormat = getModuleInfoFormat(commandDescriptions[1]);
        switch (moduleInfoFormat) {
        case "mc":
            calculator = new CapCalculatorByMc(input);
            break;
        case "code":
            calculator = new CapCalculatorByCode(input);
            break;
        default:
            //handle later
            System.out.println("ERROR");
        }
    }

    @Override
    public CommandResult executeCommand() throws KolinuxException {        
        //get cap from stored calculator
        String cap = calculator.getCap();
        String capMessage = "Your CAP for this semester will be " + cap + " if you get your desired grades!";
        logger.log(Level.INFO, "User calculated CAP");
        return new CommandResult(capMessage);
    }
}
