package DL.Game.MatchEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:     X
 * ID:              X
 **/
public class EventLog {

    private List<Event> eventList;

    public EventLog() {
        this.eventList = new ArrayList<>();
    }

    public boolean addMatchEvent(Event event) {
        if (event != null) {
            eventList.add(event);
            return true;
        }
        return false;
    }

    public List<Event> getEvents() {

        return eventList;
    }

}
