package seedu.kolinux.timetable;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleList;
import seedu.kolinux.timetable.lesson.Lesson;
import seedu.kolinux.timetable.subcommand.AddSubCommand;
import seedu.kolinux.timetable.subcommand.DeleteSubCommand;
import seedu.kolinux.timetable.subcommand.UpdateSubCommand;
import seedu.kolinux.timetable.subcommand.ViewSubCommand;
import seedu.kolinux.timetable.subcommand.SubCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static seedu.kolinux.timetable.lesson.Lesson.days;
import static seedu.kolinux.timetable.lesson.Lesson.getIndex;

/**
 * Timetable class that represents the methods to interact with the 2D timetable array and Array list for storage.
 */
public class Timetable {

    public static ModuleList moduleList;
    private static final int ROW_SIZE = 31;
    private static final int COLUMN_SIZE = 6;
    public static String [][] timetableData = new String[ROW_SIZE][COLUMN_SIZE];
    public static ArrayList<Lesson> lessonStorage = new ArrayList<>();
    public static TimetableStorage timetableStorage = new TimetableStorage(lessonStorage);
    private ViewSubCommand viewSubCommand = new ViewSubCommand();
    private AddSubCommand addSubCommand = new AddSubCommand();
    private DeleteSubCommand deleteSubCommand = new DeleteSubCommand();
    private UpdateSubCommand updateSubCommand = new UpdateSubCommand();
    public static String filePath = "./data/timetable.txt";
    public static File file = new File(filePath);
    public static final String CORRUPT_STORAGE = "Your timetable storage file is corrupted, "
            +
            "it will be reset and cleared";

    public Timetable(ModuleList moduleList) {
        this.moduleList = moduleList;
    }

    /**
     * Initializes the timetable with the data from timetable.txt when Kolinux starts up.
     *
     * @throws KolinuxException If the format of the data in the file is incorrect
     */
    public void initTimetable() throws KolinuxException {
        ArrayList<String> fileContents = new ArrayList<>();
        try {
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                fileContents.add(s.nextLine());
            }
            loadContent(fileContents);
        } catch (FileNotFoundException exception) {
            timetableStorage.createFilePath(filePath);
        } catch (ArrayIndexOutOfBoundsException | KolinuxException exception) {
            clearTimetable();
            timetableStorage.clearFile();
            throw new KolinuxException(CORRUPT_STORAGE);
        }
    }

    /**
     * Loads the content of the timetable.txt file onto the 2D timetable array and lessonStorage array list
     * to carry out any timetable commands given by user.
     *
     * @param fileContents Array list of the contents of the timetable text file
     * @throws KolinuxException If the format of the file content for timetable inputting is incorrect
     */
    private void loadContent(ArrayList<String> fileContents) throws KolinuxException {
        for (String fileContent : fileContents) {
            String[] content = fileContent.split("/");
            addSubCommand.inputLesson(content, false, true);
        }
    }

    /**
     * Executes the view subcommand for timetable which displays the timetable on CLI.
     */
    public void executeView() {
        viewSubCommand.viewTimetable();
    }

    /**
     * Executes the add subcommand for timetable which adds a lesson to the timetable.
     *
     * @param lessonDetails Details of the lesson to be added
     * @param isAllowingAdd Boolean to check whether user allows adding to timetable despite exceeding workload
     * @throws KolinuxException If it fails any of the checks in addSubCommand before performing add operation
     */
    public void executeAdd(String[] lessonDetails, boolean isAllowingAdd) throws KolinuxException {
        addSubCommand.inputLesson(lessonDetails, isAllowingAdd, false);
    }

    /**
     * Executes the delete subcommand for timetable which deletes a lesson from the timetable.
     *
     * @param lessonDetails Details of the lesson to be deleted
     * @throws KolinuxException If it fails any of the checks in DeleteSubCommand before performing delete operation
     */
    public void executeDelete(String[] lessonDetails) throws KolinuxException {
        try {
            String moduleCode = lessonDetails[0].toUpperCase();
            String lessonType = lessonDetails[1].toUpperCase();
            String day = lessonDetails[2].toLowerCase();
            String startTime = lessonDetails[3];
            deleteSubCommand.deleteLesson(moduleCode, lessonType, day, startTime);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new KolinuxException(SubCommand.INVALID_DELETE_FORMAT);
        }
    }

    /**
     * Executes the update subcommand for timetable which updates a lesson to a different timing in the timetable.
     *
     * @param lessonDetails Details of the lesson to be updated
     * @throws KolinuxException If it fails any of the checks in UpdateSubCommand before performing update operation
     */
    public void executeUpdate(String[] lessonDetails) throws KolinuxException {
        try {
            String moduleCode = lessonDetails[0].toUpperCase();
            String lessonType = lessonDetails[1].toUpperCase();
            String oldDay = lessonDetails[2].toLowerCase();
            String oldStartTiming = lessonDetails[3];
            String newDay = lessonDetails[4].toLowerCase();
            String newStartTiming = lessonDetails[5];
            updateSubCommand.updateTimetable(moduleCode, lessonType, oldDay, oldStartTiming, newDay, newStartTiming);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new KolinuxException(SubCommand.INVALID_UPDATE_FORMAT);
        }
    }

    /**
     * Deletes all lessons of a specific module code in the timetable.
     *
     * @param moduleCode Module code lessons which needs to be deleted
     */
    public void deleteByModuleList(String moduleCode) {
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COLUMN_SIZE; j++) {
                if (timetableData[i][j] != null && timetableData[i][j].contains(moduleCode)) {
                    timetableData[i][j] = null;
                }
            }
        }
        lessonStorage.removeIf(lesson -> lesson.getModuleCode().equals(moduleCode));
        timetableStorage.writeToFile();
    }

    /**
     * Lists the lessons and their timings in the timetable for a specific weekday.
     *
     * @param day The day of the lessons to be listed from
     * @throws KolinuxException If invalid day entered
     */
    public void listTimetable(String day) throws KolinuxException {
        boolean doesLessonExist = false;
        String[] lessonList = new String[ROW_SIZE];
        if (getIndex(day, days) == -1) {
            throw new KolinuxException("Please enter a valid weekday from monday to friday spelt fully");
        }
        for (Lesson lesson: lessonStorage) {
            if (lesson.getDay().equals(day)) {
                doesLessonExist = true;
                // Stores the lesson details into lessonList in the format START_TIME - END_TIME LESSON_DESCRIPTION
                lessonList[lesson.getStartTimeIndex() - 1] = lesson.getStartTime() + " - " + lesson.getEndTime()
                        + " " + lesson.getModuleCode() + " " + lesson.getLessonType();
            }
        }
        for (int i = 0; i < ROW_SIZE - 1; i++) {
            if (lessonList[i] != null) {
                System.out.println(lessonList[i]);
            }
        }
        if (!doesLessonExist) {
            System.out.println("You have no lessons on " + day);
        }
    }


    /**
     * Clears all the entries of the timetable, ending up with an empty timetable.
     */
    public void clearTimetable() {
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COLUMN_SIZE; j++) {
                timetableData[i][j] = null;
            }
        }
        lessonStorage.clear();
        timetableStorage.clearFile();
    }

}
