package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.Notifiable;
import DL.Users.Notification;
import DL.Users.User;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Description:       This abstract class represents a game Event.
 *                    an eventUser can only enter one event per gameTime to the eventLog
 **/
@NamedQueries(value = {
        @NamedQuery(name = "Events", query = "Select e From Event e")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EVENT_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Event implements Serializable, Notifiable {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Referee createdByUser;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public abstract String getType();

    public int getEventGameTime() { return gameTime; }

    @Override
    public Notification getNotification() {

        //return new Notification(toString());
        return new Notification("Goal!!!");
    }

    @Override
    public Set getNotifyUsersList()
    {
//        Set<User> result = new HashSet<>();
//
//        Team homeTeam = eventLog.getMyMatch().getHomeTeam();
//        Team awayTeam = eventLog.getMyMatch().getAwayTeam();
//
//        result.addAll(homeTeam.getTeamMembers());
//        result.addAll(homeTeam.getPage().getFollowers());
//        result.addAll(awayTeam.getTeamMembers());
//        result.addAll(awayTeam.getPage().getFollowers());
//
//        return result;

        Set<User> res = new HashSet<>();
        res.add(new Fan("admin", "admin", "admin"));
        return res;
    }
}
