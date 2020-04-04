package DL.Game.MatchEvents;

import javax.persistence.*;

/**
 * Description:       This abstract class represents a game Event.
 *                    an eventUser can only enter one event per gameTime to the eventLog
 **/

@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "Events", query = "Select e From Event e")
})
@IdClass(Event.EntryPK.class)
public abstract class Event {

    /**
     * For Composite Primary Key
     */
    public class EntryPK {
        public EventUser createdByUser;
        public EventLog eventLog;
    }

    @Id
    @OneToOne(cascade = CascadeType.MERGE)
    private EventUser createdByUser;
    @Id
    @OneToOne(cascade = CascadeType.MERGE)
    private EventLog eventLog;
    @Column
    private int gameTime;


    /**
     * Constructor
     *
     * @param createdByUser - the user who created the event
     * @param eventLog      - the event log of the required match
     * @param gameTime      - when the event occurred during the game
     */
    public Event(EventUser createdByUser, EventLog eventLog, int gameTime) {
        this.gameTime = gameTime;
        this.eventLog = eventLog;
        this.createdByUser = createdByUser;
    }

    /**
     * Default Constructor
     */
    public Event() {
        this(null, null, 0);
    }
}
