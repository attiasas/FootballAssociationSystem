package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.TeamUser;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:       This abstract class represents a game Event.
 *                    an eventUser can only enter one event per gameTime to the eventLog
 **/


@NamedQueries(value = {
        @NamedQuery(name = "Events", query = "Select e From Event e")
})

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(Event.EntryPK.class)
public abstract class Event {

    /**
     * For Composite Primary Key
     */
    public class EntryPK implements Serializable {
        public Referee createdByUser;
        public EventLog eventLog;
    }

    @Id
    @OneToOne(cascade = CascadeType.MERGE)
    private Referee createdByUser;

    @Id
    @OneToOne(cascade = CascadeType.MERGE)
    private EventLog eventLog;

    //@ManyToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    //TeamUser teamUser;

    @Column
    private int gameTime;


    /**
     * Constructor
     *
     * @param createdByUser - the user who created the event
     * @param eventLog      - the event log of the required match
     * @param gameTime      - when the event occurred during the game
     */
    public Event(Referee createdByUser, EventLog eventLog, int gameTime) {
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
