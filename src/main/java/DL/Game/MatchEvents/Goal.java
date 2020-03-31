package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public class Goal extends OnePlayerEvent {

    public Goal(int gameTime, Date timeStamp, String description, Player scorePlayer) {
        super(gameTime, timeStamp, description, scorePlayer);
    }
}
