package seedu.kolinux.timetable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDb;
import seedu.kolinux.module.ModuleList;
import seedu.kolinux.timetable.lesson.Lecture;
import seedu.kolinux.timetable.lesson.Lesson;
import seedu.kolinux.timetable.lesson.Tutorial;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.kolinux.timetable.Timetable.CORRUPT_STORAGE;


public class TimetableStorageTest {

    private ModuleList moduleList = new ModuleList();
    private ArrayList<Lesson> lessonStorage = new ArrayList<>();
    private Timetable timetable  = new Timetable(moduleList);
    private TimetableStorage timetableStorage = new TimetableStorage(lessonStorage);
    private static final ModuleDb moduleDb = new ModuleDb();

    private static final String[] VALID_STORAGE = new String[]{"CS1010","LEC","friday","1200","1300"};
    private static final String VALID_FILE_CONTENT = "CS1010/LEC/friday/1200/1300";

    @BeforeAll
    public static void initModuleDb() {
        moduleDb.initModuleDb();
    }

    @BeforeEach
    public void clear() {
        moduleList.clear();
    }

    @Test
    public void initTimetable_initWithValidLesson_timetableInitCorrectly() throws KolinuxException {
        moduleList.addModuleByCode("CS1010", moduleDb);
        Lesson lesson = new Lecture(VALID_STORAGE);
        lessonStorage.add(lesson);
        timetableStorage.writeToFile();
        lessonStorage.clear();
        timetable.initTimetable();
        assertEquals(timetable.lessonStorage.get(0).getModuleCode(), "CS1010");
    }

    @Test
    public void initTimetable_initWithInvalidLesson_timetableStorageCorrupted() throws KolinuxException {
        try {
            Lesson lesson = new Lecture(VALID_STORAGE);
            lessonStorage.add(lesson);
            timetableStorage.writeToFile();
            lessonStorage.clear();
            timetable.initTimetable();
            assertEquals(timetable.lessonStorage.get(0).getModuleCode(), "CS1010");
        } catch (KolinuxException exception) {
            assertEquals(CORRUPT_STORAGE, exception.getMessage());
        }
    }

    @Test
    public void getFileContent_addValidLesson_correctFileContent() throws KolinuxException {
        moduleList.addModuleByCode("CS1010", moduleDb);
        Lesson lesson = new Lecture(VALID_STORAGE);
        lessonStorage.add(lesson);
        String fileContent = lesson.getFileContent();
        assertEquals(fileContent,VALID_FILE_CONTENT);
    }

}
