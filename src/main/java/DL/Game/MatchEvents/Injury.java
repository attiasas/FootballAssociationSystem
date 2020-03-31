package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public class Injury extends OnePlayerEvent {

    public Injury(int gameTime, Date timeStamp, String description, Player injuredPlayer) {
        super(gameTime, timeStamp, description, injuredPlayer);
    }
}
