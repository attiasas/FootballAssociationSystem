package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.Player;

import javax.persistence.Entity;

/**
 * Description:     this class represents a PlayerChange event
 **/
@Entity
public class PlayerChange extends TwoPlayersEvent {

    public PlayerChange(Referee createdByUser, EventLog eventLog, int gameTime, Player outPlayer, Player inPlayer) {
        super(createdByUser, eventLog, gameTime, outPlayer, inPlayer);
    }

    public PlayerChange() {
        super(null,null,0,null,null);
    }
}
