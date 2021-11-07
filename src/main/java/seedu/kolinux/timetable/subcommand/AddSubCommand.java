package seedu.kolinux.timetable.subcommand;

import seedu.kolinux.exceptions.ExceedWorkloadException;
import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDetails;
import seedu.kolinux.module.ModuleList;
import seedu.kolinux.timetable.lesson.Lesson;
import seedu.kolinux.timetable.lesson.Tutorial;
import seedu.kolinux.timetable.lesson.Lecture;
import seedu.kolinux.timetable.lesson.Lab;
import seedu.kolinux.timetable.lesson.Sectional;
import seedu.kolinux.timetable.lesson.Recitation;

import java.util.Objects;

import static seedu.kolinux.timetable.Timetable.lessonStorage;
import static seedu.kolinux.timetable.Timetable.timetableData;
import static seedu.kolinux.timetable.Timetable.timetableStorage;
import static seedu.kolinux.timetable.Timetable.moduleList;
import static seedu.kolinux.timetable.lesson.Lesson.getIndex;
import static seedu.kolinux.timetable.lesson.Lesson.schoolHours;
import static seedu.kolinux.timetable.lesson.Lesson.days;

/** Represents the operations and checks when adding to the timetable. */
public class AddSubCommand extends SubCommand {

    private static final int NUMBER_SLOTS_FOR_1_HOUR = 2;

    public AddSubCommand() {

    }

    /**
     * Adds lesson to timetable based on the time and day of the lesson and
     * saves the timetable to the local storage by writing to timetable.txt
     * based on the time and day of the lesson.
     *
     * @param lesson Lesson which is to be added to the timetable
     * @throws KolinuxException If the format of user input is incorrect
     */
    public void addToTimetable(Lesson lesson) throws KolinuxException {
        String moduleCode = lesson.getModuleCode();
        String lessonType = lesson.getLessonType();
        String description = moduleCode + " " + lessonType;
        int dayIndex = lesson.getDayIndex();
        int startTimeIndex = lesson.getStartTimeIndex();
        int endTimeIndex = lesson.getEndTimeIndex();
        if (!isPeriodFree(startTimeIndex, endTimeIndex, dayIndex)) {
            throw new KolinuxException(INACCESSIBLE_PERIOD);
        }
        for (int i = startTimeIndex; i < endTimeIndex; i++) {
            assert dayIndex < COLUMN_SIZE;
            assert i < ROW_SIZE;
            timetableData[i][dayIndex] = description;
        }
        lessonStorage.add(lesson);
        timetableStorage.writeToFile();
    }

    /**
     * Performs the checks before inputting the lesson into timetable.
     *
     * @param lessonDetails The details of the lessons to be added
     * @param isAllowingAdd Boolean to check if the user allows adding in the case of exceeding workload
     * @param isStorageAdd Boolean to check if it's a storage add operation or normal add  operation by user to prevent
     *                     throwing of exception due to exceeding overload when adding lessons from storage file
     * @throws KolinuxException If any of the checks before adding to timetable fails
     */
    public void inputLesson(String[] lessonDetails, boolean isAllowingAdd, boolean isStorageAdd)
            throws KolinuxException {
        try {
            checkLessonInModuleList(moduleList, lessonDetails[0].toUpperCase());
            checkLessonType(lessonDetails[1].toUpperCase(), INVALID_ADD_FORMAT);
            checkTimeAndDayForAdd(lessonDetails[2].toLowerCase(), lessonDetails[3], lessonDetails[4]);
            checkExceedingWorkload(lessonDetails, isAllowingAdd, isStorageAdd);
            sortLessonsToAdd(lessonDetails[1].toUpperCase(), lessonDetails);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new KolinuxException(INVALID_ADD_FORMAT);
        }
    }

    /**
     * Sorts the lessons according to their lesson types before adding it to the timetable.
     *
     * @param lessonType The type of lesson to be added to timetable
     * @param lessonDetails The details of the lesson to be added to timetable
     * @throws KolinuxException If the lesson type parameter input by user is invalid
     */
    private void sortLessonsToAdd(String lessonType, String[] lessonDetails) throws KolinuxException {
        switch (lessonType) {
        case "TUT":
            addToTimetable(new Tutorial(lessonDetails));
            break;
        case "LEC":
            addToTimetable(new Lecture(lessonDetails));
            break;
        case "LAB":
            addToTimetable(new Lab(lessonDetails));
            break;
        case "SEC":
            addToTimetable(new Sectional(lessonDetails));
            break;
        case "REC":
            addToTimetable(new Recitation(lessonDetails));
            break;
        default:
            throw new KolinuxException(INVALID_LESSON_FORMAT);
        }
    }

    /**
     * Gets the lesson duration of a specific module lesson which is in the storage.
     *
     * @param moduleCode The module code of the lesson to obtain duration from
     * @param lessonType The lesson type of the lesson to obtain duration from
     * @return The duration of the specified lesson of module code and lesson type
     */
    private int getStorageHours(String moduleCode, String lessonType) {
        int hourCount = 0;
        for (Lesson storedLesson : lessonStorage) {
            if (storedLesson.getModuleCode().equals(moduleCode)
                    && storedLesson.getLessonType().equals(lessonType)) {
                hourCount += storedLesson.getLessonDuration();
            }
        }
        return hourCount;
    }

    /**
     * Gets the specified required hours for the lesson being added by user, prescribed in the api.
     *
     * @param moduleList The list of modules added by user using {@code module add} before adding to timetable
     * @param moduleCode Module code of the lesson to retrieve the workload hours from
     * @param lessonType Lesson type of the lesson to retrieve the workload hours from
     * @return The workload hours for the lesson being added
     */
    private double getRequiredHours(ModuleList moduleList, String moduleCode, String lessonType) {
        for (ModuleDetails module : moduleList.myModules) {
            if ((lessonType.equals("TUT") || lessonType.equals("REC")) && module.moduleCode.equals(moduleCode)) {
                //As the slots in timetable are in multiples of 30 mins,
                // workload hours have been multiplied by 2 for easy comparison
                return module.getTutorialHours() * NUMBER_SLOTS_FOR_1_HOUR;
            } else if ((lessonType.equals("LEC") || lessonType.equals("SEC"))
                    && module.moduleCode.equals(moduleCode)) {
                //As the slots in timetable are in multiples of 30 mins,
                // workload hours have been multiplied by 2 for easy comparison
                return module.getLectureHours() * NUMBER_SLOTS_FOR_1_HOUR;
            } else if ((lessonType.equals("LAB") || lessonType.equals("REC"))
                    && module.moduleCode.equals(moduleCode)) {
                //As the slots in timetable are in multiples of 30 mins,
                // workload hours have been multiplied by 2 for easy comparison
                return module.getLabHours() * NUMBER_SLOTS_FOR_1_HOUR;
            }

        }
        return 0;
    }

    /**
     * Checks if the lesson entered by user is stored in the {@code moduleList} first.
     *
     * @param moduleList The list of modules added by user using {@code module add} before adding to timetable
     * @param moduleCode Module code of the lesson
     * @throws KolinuxException If the module is not in the {@code moduleList}
     */
    private void checkLessonInModuleList(ModuleList moduleList, String moduleCode) throws KolinuxException {
        boolean isFound = false;
        for (ModuleDetails module : moduleList.myModules) {
            if (Objects.equals(module.moduleCode, moduleCode)) {
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            throw new KolinuxException(moduleCode + " not found in module list");
        }
    }

    /**
     * Checks if the lesson entered exceeds the prescribed workload in the api.
     *
     * @param lessonDetails The details of the lesson to be added to timetable
     * @param isAllowingAdd Boolean to check if the user allows adding in the case of exceeding workload
     * @param isStorageAdd Boolean to check if it's a storage add operation or normal add  operation by user to prevent
     *                     throwing of exception due to exceeding overload when adding lessons from storage file
     * @throws KolinuxException If the lesson's duration input by user exceeds the total workload for that
     *                          module lesson type's prescribed workload
     */
    private void checkExceedingWorkload(String[] lessonDetails, boolean isAllowingAdd, boolean isStorageAdd)
            throws KolinuxException {
        String lessonType = lessonDetails[1].toUpperCase();
        String moduleCode = lessonDetails[0].toUpperCase();
        double requiredHours = getRequiredHours(moduleList, moduleCode, lessonType);
        double inputHours = getIndex(lessonDetails[4], schoolHours) - getIndex(lessonDetails[3], schoolHours);
        double storageHours = getStorageHours(moduleCode, lessonType) + inputHours;
        if (storageHours > requiredHours && !isAllowingAdd && !isStorageAdd) {
            throw new ExceedWorkloadException("Input hours for " + moduleCode + " " + lessonType
                    +
                    " exceeds the total workload\nIt exceeds " + requiredHours / NUMBER_SLOTS_FOR_1_HOUR + " hours\n"
                    +
                    "Do you want to continue adding the lesson despite\n"
                    +
                    "exceeding the workload? Please enter y or n");
        }
    }

    /**
     * Checks if a specific time slot is occupied by another lesson.
     *
     * @param startIndex The index of the starting time of lesson in {@code schoolHours} array
     * @param endIndex The index of the ending time of lesson in {@code schoolHours} array
     * @param dayIndex The index of the day of lesson in {@code days} array
     * @return True if the time slot is not occupied by another lesson and false otherwise
     * @throws KolinuxException If the day or timings entered are invalid
     */
    private boolean isPeriodFree(int startIndex, int endIndex, int dayIndex) throws KolinuxException {
        try {
            for (int i = startIndex; i < endIndex; i++) {
                if (timetableData[i][dayIndex] != null) {
                    return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new KolinuxException(INVALID_DAY_TIME_FOR_ADD);
        }
    }

    /**
     * Checks if the time and day entered when inputting the lesson is valid.
     *
     * @param day The day of the lesson to be added
     * @param startTime The starting time of the lesson to be added
     * @param endTime The ending time of the lesson to be added
     * @throws KolinuxException If the day or timings entered are invalid
     */
    private void checkTimeAndDayForAdd(String day, String startTime, String endTime)
            throws KolinuxException {
        int dayIndex = getIndex(day, days);
        int startTimeIndex = getIndex(startTime, schoolHours);
        int endTimeIndex = getIndex(endTime, schoolHours);
        if (startTimeIndex == INVALID_INDEX || dayIndex == INVALID_INDEX || endTimeIndex == INVALID_INDEX
                || startTimeIndex >= endTimeIndex) {
            throw new KolinuxException(INVALID_ADD_FORMAT + "\n\n" + INVALID_DAY_TIME_FOR_ADD);
        }
    }

}
