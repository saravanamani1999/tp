package seedu.kolinux.capcalculator;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.CalculatorModuleList;
import seedu.kolinux.module.ModuleDb;
import seedu.kolinux.module.ModuleDetails;
import seedu.kolinux.module.ModuleList;

import static seedu.kolinux.module.Grade.A_PLUS_GRADE;
import static seedu.kolinux.module.Grade.A_MINUS_GRADE;
import static seedu.kolinux.module.Grade.A_GRADE;
import static seedu.kolinux.module.Grade.B_PLUS_GRADE;
import static seedu.kolinux.module.Grade.B_MINUS_GRADE;
import static seedu.kolinux.module.Grade.B_GRADE;
import static seedu.kolinux.module.Grade.C_PLUS_GRADE;
import static seedu.kolinux.module.Grade.C_GRADE;
import static seedu.kolinux.module.Grade.D_PLUS_GRADE;
import static seedu.kolinux.module.Grade.D_GRADE;
import static seedu.kolinux.module.Grade.F_GRADE;
import static seedu.kolinux.module.Grade.S_GRADE;
import static seedu.kolinux.module.Grade.U_GRADE;
import static seedu.kolinux.module.Grade.CS_GRADE;
import static seedu.kolinux.module.Grade.CU_GRADE;
import static seedu.kolinux.module.Grade.EXE_GRADE;
import static seedu.kolinux.module.Grade.IC_GRADE;
import static seedu.kolinux.module.Grade.IP_GRADE;
import static seedu.kolinux.module.Grade.W_GRADE;


/**
 * Represents CAP calculator used when the user's input module descriptions are based on module code.
 */
public class CapCalculatorByCode extends CapCalculator {
    
    private static final int VALID_GRADE = 0;
    private static final int INVALID_SU_GRADE = 1;
    private static final int INVALID_GRADE = 2;
    private static final int INVALID_CSCU_MODULE_MESSAGE = 3;

    protected ModuleDb moduleDb;
    
    private CalculatorModuleList repeatedModules;
    
    private int containsValidGrade(String moduleCode, String moduleGrade) {
        ModuleDetails module = moduleDb.getModuleInfo(moduleCode);
        if (module.isCsCuModule()) {
            if (moduleGrade.equals(CS_GRADE) || moduleGrade.equals(CU_GRADE)) {
                return VALID_GRADE;
            }
            return INVALID_CSCU_MODULE_MESSAGE;
        }
        if (moduleGrade.equals(A_PLUS_GRADE) || moduleGrade.equals(A_GRADE) || moduleGrade.equals(A_MINUS_GRADE)
                || moduleGrade.equals(B_PLUS_GRADE) || moduleGrade.equals(B_GRADE) || moduleGrade.equals(B_MINUS_GRADE)
                || moduleGrade.equals(C_PLUS_GRADE) || moduleGrade.equals(C_GRADE) || moduleGrade.equals(D_PLUS_GRADE)
                || moduleGrade.equals(D_GRADE) || moduleGrade.equals(F_GRADE) || moduleGrade.equals(EXE_GRADE) 
                || moduleGrade.equals(IC_GRADE) || moduleGrade.equals(IP_GRADE) || moduleGrade.equals(W_GRADE)) {
            return VALID_GRADE;
        }
        if (moduleGrade.equals(S_GRADE) || moduleGrade.equals(U_GRADE)) {
            return module.isSuAble() ? VALID_GRADE : INVALID_SU_GRADE;
        }
        return INVALID_GRADE;        
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
            int checkGradeResult = containsValidGrade(moduleCode, grade);
            if (checkGradeResult == INVALID_SU_GRADE || checkGradeResult == INVALID_CSCU_MODULE_MESSAGE) {
                invalidGradeModules.add(moduleDescription);
            }
            if (checkGradeResult == INVALID_GRADE) {
                invalidModules.add(moduleDescription);
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
        StringBuilder invalidModulesMessage = new StringBuilder("Invalid module info found: ");
        StringBuilder invalidSuModulesMessage = 
                new StringBuilder("The following module(s) contain invalid grading basis: ");
        StringBuilder repeatedModulesMessage = 
                new StringBuilder("The following module(s) are entered multiple times: ");
        boolean hasInvalidModules = false;
        boolean hasInvalidSuModules = false;
        boolean hasRepeatedModules = false;
        
        if (!invalidModules.isEmpty()) {
            hasInvalidModules = true;
            for (String module : invalidModules) {
                invalidModulesMessage.append(module).append(" ");
            }
        }
        if (!invalidGradeModules.isEmpty()) {
            hasInvalidSuModules = true;
            for (String module : invalidGradeModules) {
                invalidSuModulesMessage.append(module).append(" ");
            }
        }
        if (!(repeatedModules.getMyModulesSize() == 0)) {
            hasRepeatedModules = true;
            for (ModuleDetails module : repeatedModules.getMyModules()) {
                repeatedModulesMessage.append(module.getModuleCode()).append(" ");
            }
        }
        
        if (hasInvalidModules || hasInvalidSuModules || hasRepeatedModules) {
            String errorMessage = (hasInvalidModules ? invalidModulesMessage.toString() + "\n" : "")
                    + (hasRepeatedModules ? repeatedModulesMessage.toString() + "\n" : "")
                    + (hasInvalidSuModules ? invalidSuModulesMessage.toString() : "");
            throw new KolinuxException(errorMessage.trim());
        }    
    }
}
