package seedu.kolinux.planner;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDetails;
import seedu.kolinux.module.ModuleList;

import java.util.ArrayList;

/** Represents the methods to fetch exams data from the module list. */
public class ExamsGetter {

    private ArrayList<Event> exams = new ArrayList<>();
    private ModuleList moduleList;

    private static final int EVENT_ARGUMENTS = 4;
    private static final String FATAL_ERROR = "Fatal error occurred, please restart Kolinux.";
    private static final String COLON = ":";
    private static final String EMPTY_STRING = "";

    /**
     * Constructed when user needs to perform add, list, or delete operations in planner. Hence, the
     * module list will always be updated with the latest version.
     *
     * @param moduleList Module list stored by the user
     */
    public ExamsGetter(ModuleList moduleList) {
        this.moduleList = moduleList;
        retrieveModuleExams();
    }

    /**
     * Iterates through the module list stored by the user and creates an event from each exam information found
     * if any. This event is added to the exams list. Hence, at the end of this method, exams will be populated
     * with events that are created by the exam information of all modules stored by the user.
     */
    private void retrieveModuleExams() {
        String[] arguments = new String[EVENT_ARGUMENTS];
        for (ModuleDetails module : moduleList.getMyModules()) {
            // for modules that do not have an official final exam
            if (module.getExamDateTime() == null) {
                continue;
            }
            arguments[0] = module.getModuleCode() + " Exam";
            arguments[1] = module.getExamDate();
            arguments[2] = module.getExamStartTime().replaceFirst(COLON, EMPTY_STRING);
            arguments[3] = module.getExamEndTime().replaceFirst(COLON, EMPTY_STRING);

            try {
                Event event = new Event(arguments);
                event.setIsIntegratedEvent();
                exams.add(event);
            } catch (KolinuxException exception) {
                // Should not execute this
                System.out.println(FATAL_ERROR);
            }
        }
    }

    /**
     * Gets the list of all exams of the modules stored in the module list as an Event list.
     *
     * @return List of exams as events.
     */
    public ArrayList<Event> getExams() {
        return exams;
    }
}
