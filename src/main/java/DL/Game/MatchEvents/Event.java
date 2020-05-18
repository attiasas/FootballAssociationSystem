package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Team;
import DL.Users.Notifiable;
import DL.Users.Notification;
import DL.Users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Description:       This abstract class represents a game Event.
 *                    an eventUser can only enter one event per gameTime to the eventLog
 **/

@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "Events", query = "Select e From Event e")
})
@IdClass(Event.EntryPK.class)
public abstract class Event implements Serializable, Notifiable {

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

    public abstract String getType();

    public int getEventGameTime() { return gameTime; }

    @Override
    public Notification getNotification() {
        return new Notification(toString());
    }

    @Override
    public Set getNotifyUsersList()
    {
        Set<User> result = new HashSet<>();

        Team homeTeam = eventLog.getMyMatch().getHomeTeam();
        Team awayTeam = eventLog.getMyMatch().getAwayTeam();

        result.addAll(homeTeam.getTeamMembers());
        result.addAll(homeTeam.getPage().getFollowers());
        result.addAll(awayTeam.getTeamMembers());
        result.addAll(awayTeam.getPage().getFollowers());

        return result;
    }
}
