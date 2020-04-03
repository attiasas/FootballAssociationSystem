package DL.Game.MatchEvents;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     this class represents the event log of a game
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "EventLogs", query = "Select e From EventLog e")
})
public class EventLog {

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Event> eventList;

    /**
     * Default Constructor
     */
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
