package seedu.kolinux.capcalculator;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.CalculatorModuleList;

import java.util.ArrayList;

/**
 * Abstract representation of CAP calculator based on user input.
 */
public abstract class CapCalculator {
    
    private static final int CLASSNAME_POSITION = 3;
    
    protected static final double MAX_CAP = 5.0;

    protected static final String DIVIDER = "/";
    
    protected static final String TWO_DECIMAL_FORMAT = "%.2f";
    
    protected CalculatorModuleList modules;
    protected ArrayList<String> invalidGradeModules;
    protected ArrayList<String> invalidModules;

    /**
     * Construct this object by initializing modules and invalidModules attributes.
     */
    public CapCalculator() {
        modules = new CalculatorModuleList();
        invalidGradeModules = new ArrayList<>();
        invalidModules = new ArrayList<>();
    }

    /**
     * Check if the modules attribute in this object is empty.
     * 
     * @throws KolinuxException If the modules attribute is empty in order to show an error message to the user.
     */
    protected void checkModulesNotEmpty() throws KolinuxException {
        if (modules.getMyModulesSize() > 0 || !invalidModules.isEmpty() || !invalidGradeModules.isEmpty()) {
            return;
        }
        String errorMessage;
        String className = this.getClass().getName().split("\\.")[CLASSNAME_POSITION];
        switch (className) {
        case "CapCalculatorByCode":
            errorMessage = "Please enter valid module description. Example: CG2027/A+";
            throw new KolinuxException(errorMessage);
        case "CapCalculatorByMc":
            errorMessage = "Please enter valid module description. Example: 4/A+";
            throw new KolinuxException(errorMessage);
        case "ModuleListCapCalculator":
        case "GradeSuggestionCalculator":
            errorMessage = "Please store modules using module store command";
            throw new KolinuxException(errorMessage);
        default:
            // Should not reach this case
            assert false;
            errorMessage = "Unexpected class name found";
            throw new KolinuxException(errorMessage);
        }
    }
    
    /**
     * Calculate CAP based on a previously calculated CAP and the current module.
     *
     * @param totalMc The total modular credit of the previously calculated CAP.
     * @param cap The previously calculated CAP.
     * @param mc The modular credit of the current module.
     * @param gradePoint The grade point of the current module to be calculated into CAP.
     * @return The overall CAP up to the current module.
     */
    protected double calculateCurrentCap(int totalMc, double cap, int mc, double gradePoint) {
        return ((cap * totalMc) + (gradePoint * mc)) / (totalMc + mc);
    }

    /**
     * Calculate overall CAP based on input modules from user.
     *
     * @return The overall CAP formatted to two decimal places.
     */
    protected abstract String getCap();

    /**
     * Check if this object detects any invalid input module description from user.
     *
     * @throws KolinuxException When invalid module descriptions are found. Show an error message to the user
     *     containing all the invalid module descriptions.
     */
    protected void checkInvalidModules() throws KolinuxException {
        if (!(invalidModules.isEmpty())) {
            StringBuilder errorMessage = new StringBuilder("Invalid module info found: ");
            for (String module : invalidModules) {
                errorMessage.append(module).append(" ");
            }
            throw new KolinuxException(errorMessage.toString());
        }
    }

    /**
     * Calculate CAP based on modules stored in this calculator object.
     *
     * @return Overall CAP of the modules, formatted to two decimal places.
     * @throws KolinuxException When a module description contains an invalid module or grade.
     */
    public String executeCapCalculator() throws KolinuxException {
        checkModulesNotEmpty();
        String cap = getCap();
        checkInvalidModules();
        return cap;
    }
}
