package DL.Game.MatchEvents;

import DL.Game.Match;

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

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "eventLog" ,cascade = CascadeType.MERGE)
    private List<Event> eventList;

    @OneToOne
    private Match myMatch;

    public EventLog(Match match)
    {
        this.myMatch = match;
        this.eventList = new ArrayList<>();
    }

    /**
     * Default Constructor
     */
    public EventLog() {
        this(null);
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

    public Match getMyMatch() {
        return myMatch;
    }
}
