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
}
