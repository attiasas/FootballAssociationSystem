package DL.Game.MatchEvents;

import javax.persistence.Entity;

/**
 * Description:     this class represents a PlayerChange event
 **/
@Entity
public class PlayerChange extends TwoPlayersEvent {

    public PlayerChange(EventUser createdByUser, EventLog eventLog, int gameTime, String firstPlayer, String secondPlayer) {
        super(createdByUser, eventLog, gameTime, firstPlayer, secondPlayer);
    }

    public PlayerChange() {
        super(null,null,0,null,null);
    }
}
