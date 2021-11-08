package seedu.kolinux.planner;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleList;
import seedu.kolinux.util.Parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Represents the operations to interact with the user schedule. */
public class Planner {

    private PlannerStorage plannerStorage = new PlannerStorage();
    private ModuleList moduleList;

    private static ArrayList<Event> scheduleOfAllDates = new ArrayList<>();

    private static final String PLANNER_CORRUPTED_ERROR =
            "Some of your planner events are corrupted, they will be removed from your planner!";
    private static final String TIME_CONFLICT_PROMPT =
            "You already have an event ongoing for that time period, do you still want to add?\n"
                    + "You may enter 'n' to cancel and proceed to list the events on the date to see what you already "
                    + "planned on that day\n"
                    + "Or you may enter 'y' to add the event";
    private static final String EMPTY_LIST_MESSAGE = "There are no events planned for this date yet!";
    private static final String INVALID_ID_ERROR = "Invalid ID given, no events were deleted.";

    private static final String DATE_PATTERN = "\\d\\d\\d\\d-\\d\\d-\\d\\d";

    /**
     * Constructs a planner without parameters, which is used for initialization and testing purposes. As no module
     * list is supplied as a parameter, this planner object is only useful for clearing events.
     */
    public Planner() {

    }

    /**
     * Constructs a planner with the module list. This should be the constructor used if the operations add, list,
     * or delete needs to be executed, since they require syncing with the module list to fetch the exam data.
     *
     * @param moduleList Module list of the user
     */
    public Planner(ModuleList moduleList) {
        this.moduleList = moduleList;
    }

    /**
     * Filters all the events including the lessons and exams in the planner by a particular date.
     *
     * @param date Date
     * @return List of events, lessons, and exams happening on the given date
     */
    private ArrayList<Event> filterPlanner(String date) {
        assert (moduleList != null);
        // Get lessons and exams from Timetable and ModuleList respectively on date
        ArrayList<Event> lessonsAndExamsAsEventsOnDate
                = new ModuleSyncer(moduleList, date).getLessonsAndExamsAsEventsOnDate();
        // Get all events stored in planner on date
        ArrayList<Event> eventsOnDate
                = (ArrayList<Event>) scheduleOfAllDates
                .stream()
                .filter(event -> date.equals(event.getDate()))
                .collect(Collectors.toList());
        // Merge both lists
        eventsOnDate.addAll(lessonsAndExamsAsEventsOnDate);
        // Return a list that is sorted by their start time
        return (ArrayList<Event>) eventsOnDate
                .stream()
                .sorted(Comparator.comparing(Event::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Checks if an event has time conflict with an existing event on the same date.
     *
     * @param eventToBeAdded The event to be added
     * @return true if there is time conflict, false otherwise.
     */
    private boolean hasTimeConflict(Event eventToBeAdded) {
        ArrayList<Event> filteredPlanner = filterPlanner(eventToBeAdded.getDate());
        String startTime = eventToBeAdded.getStartTime();
        String endTime = eventToBeAdded.getEndTime();
        for (Event event : filteredPlanner) {
            if (!(startTime.compareTo(event.getEndTime()) >= 0 || endTime.compareTo(event.getStartTime()) <= 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of data strings generated from the list of events, to be used to rewrite planner.txt.
     *
     * @return List of data strings corresponding to all the events
     */
    private ArrayList<String> returnDataStrings() {
        return (ArrayList<String>) scheduleOfAllDates.stream()
                .map(event -> event.toData())
                .collect(Collectors.toList());
    }

    /**
     * Initializes the planner by loading the previously saved schedule in planner.txt. File lines
     * that are not able to construct an Event will be removed, and the new file lines will be
     * rewritten back to the storage. Hence, only corrupted lines will be removed while the rest of
     * the file data can still be added as Events to the planner.
     *
     * @throws KolinuxException If the file cannot be read properly due to corruption
     */
    public void initPlanner() throws KolinuxException {

        boolean isCorrupted = false;
        ArrayList<String> fileLines;

        if ((fileLines = plannerStorage.readFile()) == null) {
            assert scheduleOfAllDates.isEmpty();
            return;
        }

        // To remove corrupted lines from the file while keeping the valid ones.
        Event event;
        int i = 0;
        while (i < fileLines.size()) {
            try {
                event = new Event(fileLines.get(i));
                scheduleOfAllDates.add(event);
                i++;
            } catch (KolinuxException exception) {
                isCorrupted = true;
                fileLines.remove(i);
                plannerStorage.rewriteFile(fileLines);
            }
        }

        if (isCorrupted) {
            throw new KolinuxException(PLANNER_CORRUPTED_ERROR);
        }
    }

    /**
     * Adds an event to the schedule list. This method also checks if there are conflicts between the
     * added event and the existing events, lessons, and exams.
     *
     * @param event Event
     * @param allowConflict true if the user allows the time conflict to be ignored
     * @throws KolinuxException If there is a time conflict, and it is not allowed to be ignored.
     */
    public void addEvent(Event event, boolean allowConflict) throws KolinuxException {
        if (hasTimeConflict(event) && !allowConflict) {
            throw new KolinuxException(TIME_CONFLICT_PROMPT);
        }
        scheduleOfAllDates.add(event);
        plannerStorage.writeFile(event.toData());
    }

    /**
     * Lists the events on a particular date. This method first obtains the list of events, lessons, and exams
     * on the date specified. If listing with ID is required, only the added events are listed with their
     * respective IDs. Else, all the events, lessons, and exams are listed without their IDs. This method is used
     * when the user executes the planner list and delete operations, where the IDs are only printed if
     * the user needs to delete an event. Hence, users are not allowed to delete lessons stored in the
     * timetable and exams.
     *
     * @param date Date
     * @param withId true if the list is needed to display the id of the events, false otherwise.
     * @return All the events on the date in a single concatenated string
     * @throws KolinuxException If the date specified is invalid or if there are no events planned
     *     on the date specified
     */
    public String listEvents(String date, boolean withId) throws KolinuxException {

        assert Pattern.matches(DATE_PATTERN, date);

        ArrayList<String> filteredEventStrings;
        if (withId) {
            filteredEventStrings = (ArrayList<String>) filterPlanner(date)
                    .stream()
                    .filter(event -> !event.getIsIntegratedEvent())
                    .map(event -> event.toStringWithId())
                    .collect(Collectors.toList());
        } else {
            filteredEventStrings = (ArrayList<String>) filterPlanner(date)
                    .stream()
                    .map(event -> event.toString())
                    .collect(Collectors.toList());
        }

        String eventsInOneString = Parser.concatenateStrings(filteredEventStrings);
        if (eventsInOneString.isEmpty()) {
            throw new KolinuxException(EMPTY_LIST_MESSAGE);
        }
        return eventsInOneString;
    }

    /**
     * Deletes an event given its corresponding unique ID.
     *
     * @param id Unique identifier of the event
     * @return Deleted event
     * @throws KolinuxException If the id does not match any events
     */
    public Event deleteEvent(String id) throws KolinuxException {
        Event eventToBeRemoved = null;
        for (Event event : scheduleOfAllDates) {
            if (id.equals(event.getId())) {
                eventToBeRemoved = event;
            }
        }
        if (eventToBeRemoved == null) {
            throw new KolinuxException(INVALID_ID_ERROR);
        }
        scheduleOfAllDates.remove(eventToBeRemoved);
        plannerStorage.rewriteFile(returnDataStrings());
        return eventToBeRemoved;
    }

    /**
     * Clears all events in the schedule.
     */
    public void clearEvents() {
        scheduleOfAllDates.clear();
        plannerStorage.clearFile();
    }
}
