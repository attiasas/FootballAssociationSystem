package DL.Game.MatchEvents;

import DL.Game.Referee;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Description:     this class represents an event of stoppageTime
 */
@Entity
public class StoppageTime extends Event {

    @Column
    private int addedTime;

    public StoppageTime(Referee createdByUser, EventLog eventLog, int gameTime, int addedTime) {
        super(createdByUser, eventLog, gameTime);
        this.addedTime = addedTime;
    }

    public StoppageTime() {
        super(null, null, 0);
        this.addedTime = 0;
    }

    @Override
    public String getType() { return Type(); }

    public static String Type() { return "Time Stopped"; }

    @Override
    public String toString() {
        return "Match Was Stopped, " + addedTime + " Time Added";
    }
}
