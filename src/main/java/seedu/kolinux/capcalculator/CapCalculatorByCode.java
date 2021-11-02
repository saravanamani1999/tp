package seedu.kolinux.capcalculator;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.CalculatorModuleList;
import seedu.kolinux.module.ModuleDb;
import seedu.kolinux.module.ModuleDetails;
import seedu.kolinux.module.ModuleList;

/**
 * Represents CAP calculator used when the user's input module descriptions are based on module code.
 */
public class CapCalculatorByCode extends CapCalculator {

    protected ModuleDb moduleDb;
    
    private CalculatorModuleList repeatedModules;

    private boolean isValidGrade(String moduleGrade) {
        return moduleGrade.equals("A+") || moduleGrade.equals("A") || moduleGrade.equals("A-")
                || moduleGrade.equals("B+") || moduleGrade.equals("B") || moduleGrade.equals("B-")
                || moduleGrade.equals("C+") || moduleGrade.equals("C") || moduleGrade.equals("D+")
                || moduleGrade.equals("D") || moduleGrade.equals("F") || moduleGrade.equals("S")
                || moduleGrade.equals("CS") || moduleGrade.equals("U") || moduleGrade.equals("CU")
                || moduleGrade.equals("EXE") || moduleGrade.equals("IC") || moduleGrade.equals("IP")
                || moduleGrade.equals("W");
    }

    /**
     * Read and store the modules from user's input into this calculator.
     * 
     * @param parsedArguments Array of module descriptions from user.
     */
    private void getInputModules(String[] parsedArguments) {
        if (parsedArguments.length == 1 && parsedArguments[0].equals("")) {
            return;
        }
        for (String moduleDescription : parsedArguments) {
            String inputModule = moduleDescription.toUpperCase();
            String[] moduleDescriptions = inputModule.split(DIVIDER);
            if (moduleDescriptions.length != 2) {
                invalidModules.add(moduleDescription);
                continue;
            }
            String moduleCode = moduleDescriptions[0];
            if (moduleDb.getModuleInfo(moduleCode) == null) {
                invalidModules.add(moduleDescription);
                continue;
            }
            String grade = moduleDescriptions[1];
            if (!isValidGrade(grade)) {
                invalidModules.add(moduleDescription);
                continue;
            }
            if (!modules.storeModuleCodeGrade(moduleCode, grade, moduleDb)) {
                repeatedModules.storeModuleCodeGrade(moduleCode, grade, moduleDb);
            }
        }
    }

    /**
     * Get the stored modules from Kolinux's module list and store them in this calculator.
     * 
     * @param moduleList The list of modules stored in Kolinux.
     */
    private void getInputModules(ModuleList moduleList) {
        for (ModuleDetails module : moduleList.getMyModules()) {
            if (module.containsNullGrade()) {
                invalidModules.add(module.getModuleCode() + DIVIDER + module.getGrade());
                continue;
            }
            modules.storeModule(module);
        }
    }

    /**
     * Construct the superclass of this object and initialize moduleDb in order to retrieve 
     * module information from the database. Module details are then retrieved from input string.
     * 
     * @param parsedArguments Array of module descriptions from user which contains the module codes and their grade.
     */
    public CapCalculatorByCode(String[] parsedArguments) {
        super();
        repeatedModules = new CalculatorModuleList();
        moduleDb = new ModuleDb().getPreInitModuleDb();
        getInputModules(parsedArguments);
    }

    /**
     * Constructor used when module details are retrieved from moduleList of Kolinux instead of user's input.
     * 
     * @param moduleList List of modules stored in moduleList of Kolinux.
     */
    public CapCalculatorByCode(ModuleList moduleList) {
        super();
        moduleDb = new ModuleDb().getPreInitModuleDb();
        getInputModules(moduleList);
    }

    @Override
    protected String getCap() {
        int totalMc = 0;
        double cap = 0;
        for (ModuleDetails module : modules.getMyModules()) {
            if (module.containsNonCalculatingGrade()) {
                continue;
            }

            double gradePoint = module.getGradePoint();
            
            String moduleCode = module.getModuleCode();
            ModuleDetails moduleInfo = moduleDb.getModuleInfo(moduleCode);
            String moduleCredit = moduleInfo.getModuleCredit();
            int mc = Integer.parseInt(moduleCredit);
            
            cap = calculateCurrentCap(totalMc, cap, mc, gradePoint);
            totalMc += mc;
            assert cap <= MAX_CAP;
        }
        return String.format(TWO_DECIMAL_FORMAT, cap);
    }

    @Override
    protected void checkInvalidModules() throws KolinuxException {
        StringBuilder invalidModulesMessage = new StringBuilder("Invalid module info format found: ");
        StringBuilder repeatedModulesMessage = new StringBuilder("These modules are entered multiple times: ");
        boolean hasInvalidModules = false;
        boolean hasRepeatedModules = false;
        
        if (!invalidModules.isEmpty()) {
            hasInvalidModules = true;
            for (String module : invalidModules) {
                invalidModulesMessage.append(module).append(" ");
            }
        }
        if (!(repeatedModules.getMyModulesSize() == 0)) {
            hasRepeatedModules = true;
            for (ModuleDetails module : repeatedModules.getMyModules()) {
                repeatedModulesMessage.append(module.getModuleCode()).append(" ");
            }
        }
        
        if (hasInvalidModules && hasRepeatedModules) {
            String errorMessage = repeatedModulesMessage.toString() + "\n" + invalidModulesMessage.toString();
            throw new KolinuxException(errorMessage);
        }
        if (hasInvalidModules) {
            String errorMessage = invalidModulesMessage.toString();
            throw new KolinuxException(errorMessage);
        }
        if (hasRepeatedModules) {
            String errorMessage = repeatedModulesMessage.toString();
            throw new KolinuxException(errorMessage);
        }
    }
}
