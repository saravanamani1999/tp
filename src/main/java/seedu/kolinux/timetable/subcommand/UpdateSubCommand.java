package seedu.kolinux.timetable.subcommand;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.timetable.lesson.Lesson;

import java.util.Objects;

import static seedu.kolinux.timetable.Timetable.lessonStorage;
import static seedu.kolinux.timetable.lesson.Lesson.getIndex;
import static seedu.kolinux.timetable.lesson.Lesson.schoolHours;

public class UpdateSubCommand extends SubCommand {

    DeleteSubCommand deleteSubcommand = new DeleteSubCommand();
    AddSubCommand addSubcommand = new AddSubCommand();

    public UpdateSubCommand() {

    }

    /**
     * Updates the lesson to a different timing on the timetable.
     *
     * @param moduleCode Module code of lesson to be updated
     * @param lessonType Lesson type of lesson to be updated
     * @param oldDay Old day of lesson to be updated
     * @param oldStartTiming Old starting time of lesson to be updated
     * @param newDay New day which the lesson is getting updated to
     * @param newStartTiming New starting time which the lesson is getting updated to
     * @throws KolinuxException If the lesson to be updated is not found in the timetable
     */
    public void updateTimetable(String moduleCode, String lessonType, String oldDay,
            String oldStartTiming, String newDay, String newStartTiming) throws KolinuxException {
        checkLessonType(lessonType, INVALID_UPDATE_FORMAT);
        checkStartTimeAndDay(oldDay, oldStartTiming, INVALID_UPDATE_FORMAT);
        checkStartTimeAndDay(newDay, newStartTiming, INVALID_UPDATE_FORMAT);
        if (!isLessonInTimetable(moduleCode, lessonType, oldDay, oldStartTiming)) {
            throw new KolinuxException(MISSING_LESSON_TO_UPDATE);
        }
        int startIndex = getIndex(newStartTiming, schoolHours);
        int endIndex = startIndex + getOldLessonHours(moduleCode, lessonType, oldDay, oldStartTiming);
        // Lesson ends later than 2100
        if (endIndex > 31) {
            throw new KolinuxException(INVALID_UPDATE_FORMAT + "\n\n" + INVALID_DAY_START_TIME);
        }
        String newEndTiming = schoolHours[endIndex - 1];
        checkUpdateTiming(moduleCode, lessonType, oldDay, newDay, oldStartTiming, newStartTiming, newEndTiming);
        String[] parameters = new String[] {moduleCode, lessonType, newDay, newStartTiming, newEndTiming};
        deleteSubcommand.deleteLesson(moduleCode, lessonType, oldDay, oldStartTiming);
        addSubcommand.inputLesson(parameters, true, false);
    }

    /**
     * Gets the duration of the lesson which is to be updated.
     *
     * @param moduleCode Module code of lesson to be updated
     * @param lessonType Lesson type of lesson to be updated
     * @param day Old day of lesson to be updated
     * @param oldStartTiming Old starting time of lesson to be updated
     * @return The duration of the lesson which is to be updated
     */
    private int getOldLessonHours(String moduleCode, String lessonType, String day, String oldStartTiming) {
        for (Lesson lesson : lessonStorage) {
            if (lesson.getModuleCode().equals(moduleCode) && lesson.getLessonType().equals(lessonType)
                    && lesson.getDay().equals(day) && lesson.getStartTime().equals(oldStartTiming)) {
                return lesson.getEndTimeIndex() - lesson.getStartTimeIndex();
            }
        }
        return -1;
    }

    /**
     * Gets the old ending time of the lesson to be updated.
     *
     * @param moduleCode Module code of lesson to be updated
     * @param lessonType Lesson type of lesson to be updated
     * @param day Old day of lesson to be updated
     * @param startTime Old starting time of lesson to be updated
     * @return The old ending time of the lesson to be updated
     */
    private String getOldEndTiming(String moduleCode, String lessonType, String day, String startTime) {
        String oldEndTime = null;
        for (Lesson lesson : lessonStorage) {
            if (lesson.getModuleCode().equals(moduleCode) && lesson.getLessonType().equals(lessonType)
                    && lesson.getDay().equals(day) && lesson.getStartTime().equals(startTime)) {
                oldEndTime = lesson.getEndTime();
            }
        }
        return oldEndTime;
    }

    /**
     * Checks if the lesson is getting updated to the same timing.
     *
     * @param moduleCode Module code of lesson to be updated
     * @param lessonType Lesson type of lesson to be updated
     * @param oldDay Old day of lesson to be updated
     * @param newDay New day which the lesson is getting updated to
     * @param oldStartTiming Old starting time of lesson to be updated
     * @param newStartTiming New starting time which the lesson is getting updated to
     * @param newEndTiming New ending time which the lesson is getting updated to
     * @throws KolinuxException If the lesson is getting updated to the same timing
     */
    private void checkUpdateTiming(String moduleCode, String lessonType, String oldDay, String newDay,
            String oldStartTiming, String newStartTiming, String newEndTiming) throws KolinuxException {
        String oldEndTiming = getOldEndTiming(moduleCode, lessonType, oldDay, oldStartTiming);
        if (Objects.equals(oldDay, newDay) && Objects.equals(oldStartTiming, newStartTiming)
                && Objects.equals(oldEndTiming, newEndTiming)) {
            throw new KolinuxException(UPDATING_TO_SAME_TIMING);
        }
    }

}
