package seedu.kolinux.timetable.lesson;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.timetable.subcommand.SubCommand;

/** Lesson class which stores all the attributes of a lesson to be input into the timetable. */
public abstract class Lesson {


    protected String lessonType;
    protected String moduleCode;
    protected String day;
    protected String startTime;
    protected String endTime;
    protected int startTimeIndex;
    protected int endTimeIndex;
    protected int dayIndex;
    public static String []  schoolHours = new String [] {"0600", "0630", "0700", "0730","0800", "0830", "0900",
            "0930","1000", "1030","1100", "1130", "1200", "1230", "1300", "1330", "1400", "1430", "1500",
            "1530","1600", "1630","1700", "1730", "1800", "1830","1900", "1930","2000","2030","2100"};
    public static String[] days = new String[] {"monday", "tuesday", "wednesday", "thursday", "friday"};
    private static final int INVALID_INDEX = -1;

    public Lesson(String[] parsedArguments) throws KolinuxException {
        try {
            this.moduleCode = parsedArguments[0].toUpperCase();
            this.lessonType = parsedArguments[1].toUpperCase();
            this.day = parsedArguments[2].toLowerCase();
            this.startTime = parsedArguments[3];
            this.endTime = parsedArguments[4];
            this.startTimeIndex = getIndex(startTime, schoolHours);
            this.endTimeIndex = getIndex(endTime, schoolHours);
            this.dayIndex = getIndex(day, days);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new KolinuxException(SubCommand.INVALID_ADD_FORMAT);
        }
    }

    /**
     * Gets the lesson type of the lesson.
     *
     * @return The lesson type of the lesson
     */
    public String getLessonType() {
        return lessonType;
    }

    /**
     * Gets the description of the lesson.
     *
     * @return The description of the lesson
     */
    public String getModuleCode() {
        return moduleCode;
    }

    /**
     * Gets the index of the day of the lesson in the days array.
     *
     * @return The index of the day of the lesson in the days array.
     */
    public int getDayIndex() {
        return dayIndex;
    }

    /**
     * Gets the index of the starting time of the lesson in the {@code schoolHours} array.
     *
     * @return The index of the starting time of the lesson in the {@code schoolHours} array
     */
    public int getStartTimeIndex() {
        return startTimeIndex;
    }

    /**
     * Gets the index of the ending time of the lesson in the {@code schoolHours} array.
     *
     * @return The index of the ending time of the lesson in the {@code schoolHours} array
     */
    public int getEndTimeIndex() {
        return endTimeIndex;
    }

    /**
     * Gets the starting time of a lesson.
     *
     * @return The starting time of a lesson
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the ending time of a lesson.
     *
     * @return The ending time of a lesson
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Gets the day of a lesson.
     *
     * @return The day of a lesson
     */
    public String getDay() {
        return day;
    }

    /**
     * Formats the information about the lesson with accordance to format to be saved in the timetable.txt.
     *
     * @return Formatted information about the lesson
     */
    public String getFileContent() {
        return moduleCode + "/" + lessonType + "/" + day + "/" + startTime + "/" + endTime;
    }

    /**
     * Finds the index of a given string in one of the given arrays, days or {@code schoolHours}.
     *
     * @param input The input string by user for either the day or the timings of their lesson
     * @param array The arrays, days or {@code schoolHours}, containing the days and school hours for a week
     * @return The index of the string in the array
     */
    public static int getIndex(String input, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(input)) {
                // Adds 1 as the first row and column in the timetable is occupied by the timings and days
                return i + 1;
            }
        }
        return INVALID_INDEX;
    }

    /**
     * Gets the duration of a lesson.
     *
     * @return The duration of a lesson
     */
    public int getLessonDuration() {
        return endTimeIndex - startTimeIndex;
    }

}
