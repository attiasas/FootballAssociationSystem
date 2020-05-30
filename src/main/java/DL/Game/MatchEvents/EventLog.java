package DL.Game.MatchEvents;

import DL.Game.Match;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     this class represents the event log of a game
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "EventLogs", query = "Select e From EventLog e"),
        @NamedQuery(name = "eventLogByMatch", query = "select e from EventLog e where e.myMatch = :match")
})
public class EventLog implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "eventLog" ,cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
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
