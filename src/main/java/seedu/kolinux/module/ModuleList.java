package seedu.kolinux.module;

import java.util.ArrayList;

import static seedu.kolinux.commands.TimetableCommand.timetable;

import static seedu.kolinux.module.Grade.RESET_GRADE;
import static seedu.kolinux.module.Grade.RESET_GRADE_ARGUMENT;

/**
 * ModuleList class contains and facilitate operations on the myModules list.
 */
public class ModuleList {
    public final String horizontalLine;
    public ArrayList<ModuleDetails> myModules = new ArrayList<>();

    public ArrayList<ModuleDetails> getMyModules() {
        return myModules;
    }

    public int getMyModulesSize() {
        return myModules.size();
    }

    public void clear() {
        myModules.clear();
    }


    public ModuleList() {
        horizontalLine = "....................................................................";
    }

    /**
     * Searches the myModules list for a module corresponding to the give moduleCode and returns its grade.
     *
     * @param moduleCode Module whose grade is to be returned
     * @return Returns the grade of the module whose code is moduleCode if it exists in myModules. Returns null if the
     *      module is not stored.
     */
    public String getModuleGrade(String moduleCode) {
        for (ModuleDetails module : myModules) {
            if (module.getModuleCode().equals(moduleCode)) {
                return module.getGrade();
            }
        }

        return null;
    }

    /**
     * Searches the myModules list for a module corresponding to the give moduleCode and updates its grade to the given
     * grade.
     *
     * @param moduleCode Module whose grade is to be updated
     * @param grade      New grade for the module whose code is moduleCode
     * @return Returns an acknowledgement message if the grade is updated. Returns an error message if the moduleCode is
     *      invalid
     */
    public String setModuleGrade(String moduleCode, String grade) {
        for (ModuleDetails module : myModules) {
            if (module.getModuleCode().equals(moduleCode)) {
                if (grade.equals(RESET_GRADE_ARGUMENT) || grade.equals(RESET_GRADE)) {
                    return module.resetGrade();
                }
                if (module.getGrade().equals(grade)) {
                    return moduleCode + " grade is already set to " + grade;
                }
                module.setGrade(grade);
                return moduleCode + " grade set to " + grade;
            }
        }

        return moduleCode + " not found in the list";
    }

    /**
     * Stores the moduleDetails corresponding to a given module code in the myModules list.
     *
     * @param code Module code whose details will be stored
     * @return Returns an acknowledgement message if store is successful. Returns an error message if the code is
     *      invalid, or if it already exists in the list
     */
    public String addModuleByCode(String code, ModuleDb moduleDb) {
        ModuleDetails mod = moduleDb.getModuleInfo(code);

        if (mod == null) {
            return "Please enter a valid module code";
        } else if (myModules.contains(mod)) {
            return mod.getModuleCode() + " is already in the module list";
        } else {
            myModules.add(mod);
            return "Successfully added module: " + mod.getModuleCode();
        }

    }

    /**
     * Deletes the moduleDetails corresponding to a given module code from the myModules list.
     *
     * @param code Module code whose details will be deleted
     * @return Returns an acknowledgement message if deletion is successful. Returns an error message if the code is
     *      invalid, or if it is not found in the list
     */
    public String deleteModuleByCode(String code) {
        for (int i = 0; i < myModules.size(); i++) {
            if (myModules.get(i).getModuleCode().equals(code)) {
                myModules.get(i).setGrade(RESET_GRADE);
                myModules.remove(i);
                timetable.deleteByModuleList(code);
                return "Successfully deleted module: " + code;
            }
        }
        return code + " not found in the module list";
    }

    /**
     * Gets the exam date and time for a module.
     *
     * @param module Module details containing all module information
     */
    public void getExamDateTime(ModuleDetails module) {
        String examDate = module.getExamDate();
        String examStartTime = module.getExamStartTime();
        String examEndTime = module.getExamEndTime();
        if (examDate != null && examStartTime != null && examEndTime != null) {
            System.out.println("Exam date: " + examDate);
            System.out.println("Exam time: " + examStartTime + " - " + examEndTime);
        } else {
            System.out.println("No exam");
        }
    }

    /**
     * Gets the workload for each specific lesson type of module code.
     *
     * @param module Module details containing all module information
     * @param code Module code of module
     * @param title Title of module
     */
    public void getWorkload(ModuleDetails module, String code, String title) {
        double lectureHours = module.getLectureHours();
        System.out.println(code + " " + title + "\n\nWorkload:");
        if (lectureHours > 0) {
            System.out.println("Lecture: " + lectureHours + " hours");
        }
        double tutorialHours = module.getTutorialHours();
        if (tutorialHours > 0) {
            System.out.println("Tutorial: " + tutorialHours + " hours");
        }
        double labHours = module.getLabHours();
        if (labHours > 0) {
            System.out.println("Lab: " + labHours + " hours");
        }
        double projectHours = module.getProjectHours();
        if (projectHours > 0) {
            System.out.println("Project Work: " + projectHours + " hours");
        }
        double preparationHours = module.getPreparationHours();
        if (preparationHours > 0) {
            System.out.println("Preparation: " + preparationHours + " hours");
        }
        if (preparationHours < 0 && projectHours < 0 && labHours < 0 && tutorialHours < 0 && lectureHours < 0) {
            System.out.println("No workload information available for " + code);
        }
    }

    /**
     * Prints codes and titles of each module stored in the myModules list.
     */
    public void listMyModules() {
        for (ModuleDetails module : myModules) {
            String code = module.getModuleCode();
            String title = module.getTitle();
            getWorkload(module, code, title);
            getExamDateTime(module);
            String grade = module.getGrade();
            if (grade.equals(RESET_GRADE)) {
                System.out.println("Final grade: N/A");
            } else {
                System.out.println("Final grade: " + grade);
            }
            System.out.println(horizontalLine);
        }
        System.out.print("Remember to add the module's lessons to the timetable based on the workload");
    }
}
