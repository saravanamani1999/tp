package seedu.kolinux.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gcardone.junidecode.Junidecode;

import java.util.Arrays;

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
import static seedu.kolinux.module.Grade.RESET_GRADE;

/**
 * ModuleDetails class that stores all attributes of each module.
 */
public class ModuleDetails {

    public String moduleCode;
    private String title;
    private String description;
    private String department;
    private String moduleCredit;
    private String faculty;
    private String grade;
    private double[] workload;
    private double lectureHours;
    private double tutorialHours;
    private double labHours;
    private double projectHours;
    private double preparationHours;
    private JsonArray semesterData;
    private JsonObject attributes;
    private static final int OFFSET = 8;
    private static final int SEMESTER_1 = 0;
    private static final int SEMESTER_2 = 1;
    private static final int WORD_LIMIT = 50;

    public ModuleDetails(String moduleCode, String moduleCredit, String faculty, String description, String title,
                         String department, double[] workload, JsonArray semesterData, JsonObject attributes) {

        /*Attributes read from the NUSmods JSON may contain unicode characters. For proper printing in standard output,
        these characters are converted to their ASCII equivalent.*/
        this.moduleCode = Junidecode.unidecode(moduleCode);
        this.moduleCredit = Junidecode.unidecode(moduleCredit);
        this.faculty = Junidecode.unidecode(faculty);
        this.description = Junidecode.unidecode(description);
        this.title = Junidecode.unidecode(title);
        this.department = Junidecode.unidecode(department);
        this.workload = workload;
        this.semesterData = semesterData;
        this.grade = RESET_GRADE;
        this.attributes = attributes;
        assert Integer.parseInt(this.moduleCredit) > 0 : "Modular Credits must be positive";
    }

    public ModuleDetails(int moduleCredit, String grade) {
        String mc = Integer.toString(moduleCredit);

        this.moduleCode = null;
        this.moduleCredit = mc;
        this.faculty = null;
        this.description = null;
        this.title = null;
        this.department = null;
        this.workload = null;
        this.semesterData = null;
        this.grade = grade;
        this.attributes = null;
    }

    public ModuleDetails(String moduleCode, String grade) {
        this.moduleCode = moduleCode;
        this.moduleCredit = null;
        this.faculty = null;
        this.description = null;
        this.title = null;
        this.department = null;
        this.workload = null;
        this.semesterData = null;
        this.grade = grade;
        this.attributes = null;
    }

    /**
     * Sets the grade input by user.
     *
     * @param newGrade Grade inputted by user for a module
     */
    public void setGrade(String newGrade) {
        grade = newGrade;
    }

    /**
     * Gets the grade set by user for the module.
     *
     * @return The grade set by user for the module
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Gets the Module credits of a module.
     *
     * @return Module credits for a module
     */
    public String getModuleCredit() {
        return moduleCredit;
    }

    /**
     * Gets the Module code of a module.
     *
     * @return Module code for a module
     */
    public String getModuleCode() {
        return moduleCode;
    }

    /**
     * Gets the description of a module.
     *
     * @return Description of a module
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the title of a module.
     *
     * @return Title of a module
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the department which conducts the module.
     *
     * @return Department which conducts the module
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the faculty which conducts the module.
     *
     * @return Faculty which conducts the module
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * Gets the prescribed tutorial workload hours from the json file.
     *
     * @return Tutorial workload hours
     */
    public double getTutorialHours() {
        try {
            tutorialHours = workload[1];
            return tutorialHours;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Gets the prescribed lecture workload hours from the json file.
     *
     * @return Lecture workload hours
     */
    public double getLectureHours() {
        try {
            lectureHours = workload[0];
            return lectureHours;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Gets the prescribed lab workload hours from the json file.
     *
     * @return Lab workload hours
     */
    public double getLabHours() {
        try {
            labHours = workload[2];
            return labHours;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Gets the prescribed project workload hours from the json file.
     *
     * @return Project workload hours
     */
    public double getProjectHours() {
        try {
            projectHours = workload[3];
            return projectHours;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Gets the prescribed preparation workload hours from the json file.
     *
     * @return Preparation workload hours
     */
    public double getPreparationHours() {
        try {
            preparationHours = workload[4];
            return preparationHours;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return -1;
        }
    }

    /**
     * Gets the time and date of exam for a module from the json file.
     *
     * @return The time and date of exam for a module
     */
    public String[] getExamDateTime() {
        try {
            String examDate = semesterData.get(SEMESTER_1).getAsJsonObject().get("examDate").getAsString();
            String newTimeFormat = examDate.replace(":00.000Z", "");
            String[] dateTime = newTimeFormat.split("T");
            return dateTime;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * Gets the date of exam for a module from the json file by parsing the date and time
     * which is extracted from the json file.
     *
     * @return The time and date of exam for a module
     */
    public String getExamDate() {
        try {
            String[] dateTime = getExamDateTime();
            return dateTime[0];
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * Gets the start time of exam for a module from the json file and converts it from UTC time zone to HHmm.
     *
     * @return The time and date of exam for a module
     */
    public String getExamStartTime() {
        try {
            String[] dateTime = getExamDateTime();
            String time = dateTime[1];
            String[] timings = time.split(":");
            int offsetTime = Integer.parseInt(timings[0]) + OFFSET;
            String finalTime;
            if (offsetTime < 10) {
                finalTime = "0" + offsetTime + ":00";
            } else {
                finalTime = offsetTime + ":00";
            }
            return finalTime;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * Gets the end time of exam for a module by adding the exam hours to the exam start time.
     *
     * @return The time and date of exam for a module
     */
    public String getExamEndTime() {
        try {
            int examHours = 0;
            examHours = (semesterData.get(SEMESTER_1).getAsJsonObject().get("examDuration").getAsInt()) / 60;
            String time = getExamStartTime();
            String[] timings = time.split(":");
            int endTiming = Integer.parseInt(timings[0]) + examHours;
            String endTime;
            if (endTiming < 10) {
                endTime = "0" + endTiming + ":00";
            } else {
                endTime = endTiming + ":00";
            }
            return endTime;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            return null;
        }
    }

    /**
     * Returns a String object representing the ModuleDetails in a format that can be used to read and write
     * the ModuleDetails from/to file.
     *
     * @return The String representation of the ModuleDetails that be used to read and write from/to file.
     */
    public String getEncodedFormat() {
        return moduleCode + "/" + grade;
    }

    /**
     * Return a grade point corresponding to the grade of this object.
     *
     * @return Grade point in double type
     */
    public double getGradePoint() {
        switch (grade) {
        case A_PLUS_GRADE:
            // Fallthrough, is equivalent grade point to "A"
        case A_GRADE:
            return 5.0;
        case A_MINUS_GRADE:
            return 4.5;
        case B_PLUS_GRADE:
            return 4.0;
        case B_GRADE:
            return 3.5;
        case B_MINUS_GRADE:
            return 3.0;
        case C_PLUS_GRADE:
            return 2.5;
        case C_GRADE:
            return 2.0;
        case D_PLUS_GRADE:
            return 1.5;
        case D_GRADE:
            return 1.0;
        case F_GRADE:
            return 0.0;
        default:
            return -1; // Invalid grade
        }
    }

    /**
     * Check if this module contains grade excluded from CAP calculation.
     * 
     * @return True if this module contains a non-calculating grade, false otherwise.
     */
    public boolean containsNonCalculatingGrade() {
        return grade.equals(S_GRADE) || grade.equals(CS_GRADE) || grade.equals(U_GRADE) || grade.equals(CU_GRADE)
                || grade.equals(EXE_GRADE) || grade.equals(IC_GRADE) || grade.equals(IP_GRADE) || grade.equals(W_GRADE);
    }

    /**
     * Check if this contains does not have an assigned letter grade.
     * 
     * @return True if a letter grade hasn't been assigned for this module, false otherwise.
     */
    public boolean containsNullGrade() {
        return grade.equals(RESET_GRADE);
    }

    /**
     * Reset the grade of this module.
     * 
     * @return A message indicating that the grade has been reset.
     */
    public String resetGrade() {
        if (grade.equals(RESET_GRADE)) {
            return moduleCode + " does not have final grade stored";
        }
        grade = RESET_GRADE;
        return moduleCode + " grade reset";
    }

    /**
     * Check if this module accepts C/U grade.
     *
     * @return True if S/U grade is accepted by this module, false otherwise.
     */
    public boolean isSuAble() {
        try {
            return attributes.get("su").getAsBoolean();
        } catch (NullPointerException exception) {
            return false;
        }
    }

    /**
     * Check if a module is graded using a CS/CU basis. This is a workaround method as there is no official "CS/CU"
     * property indicated within NUSMods API.
     *
     * @return True if the module is graded with CS/CU basis, false otherwise.
     */
    public boolean isCsCuModule() {
        return description.contains("CS/CU") || moduleCode.contains("GEQ1000") || moduleCode.contains("DMX")
                || moduleCode.equals("CP2106") || moduleCode.equals("CFG1002");
    }

    /**
     * Returns a String object that is formatted for printing in CLI.
     *
     * @return The String representation of a module's details
     */
    @Override
    public String toString() {
        int i = WORD_LIMIT;
        String newDescription = description.replaceAll("\n", " ");
        StringBuilder descriptionSequence = new StringBuilder(newDescription);
        while (i < newDescription.length()) {
            if ((newDescription.charAt(i) == ' ') && (newDescription.charAt(i + 1) != '\n')) {
                descriptionSequence.setCharAt(i, '\n');
                i += WORD_LIMIT;
            } else {
                i++;
            }
        }
        String formattedDescription = descriptionSequence.toString();
        return moduleCode + ": " + title + "\n" + "Department: " + department + "\n" + "Faculty: " + faculty + "\n"
                + "Credits: " + moduleCredit + "\n" + "Grade: " + (grade.equals(RESET_GRADE) ? "N/A" : grade) + "\n"
                + "Has S/U option: " + (isCsCuModule() ? "Compulsory CS/CU" : isSuAble() ? "Yes" : "No") + "\n"
                + formattedDescription;
    }

    /**
     * Override equals() to compare two ModuleDetails objects.
     *
     * @return True if all attributes of the two ModuleDetails match, false otherwise.
     */
    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof ModuleDetails)) {
            return false;
        }

        ModuleDetails castOther = (ModuleDetails) other;

        return moduleCode.equals(castOther.moduleCode)
                && title.equals(castOther.title)
                && description.equals(castOther.description)
                && department.equals(castOther.department)
                && moduleCredit.equals(castOther.moduleCredit)
                && faculty.equals(castOther.faculty)
                && grade.equals(castOther.grade)
                && Arrays.equals(workload, castOther.workload)
                && Double.valueOf(lectureHours).equals(castOther.lectureHours)
                && Double.valueOf(tutorialHours).equals(castOther.tutorialHours)
                && Double.valueOf(labHours).equals(castOther.labHours)
                && Double.valueOf(projectHours).equals(castOther.projectHours)
                && Double.valueOf(preparationHours).equals(castOther.preparationHours)
                && semesterData.equals(castOther.semesterData)
                && attributes.equals(castOther.attributes);
    }

}
