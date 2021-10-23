package seedu.kolinux.timetable.lesson;

import seedu.kolinux.exceptions.KolinuxException;

public class Lecture extends Lesson {

    public Lecture(String[] parsedArguments) throws KolinuxException {
        super(parsedArguments);
    }

    @Override
    public String getLessonType() {
        return "LEC";
    }

    @Override
    public String getFileContent() {
        return moduleCode + "/LEC/" + day + "/" + startTime + "/" + endTime;
    }

}