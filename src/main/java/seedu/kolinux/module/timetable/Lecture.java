package seedu.kolinux.module.timetable;

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
        return "LEC/" + moduleCode + "/" + day + "/" + startTime + "/" + endTime;
    }

}
