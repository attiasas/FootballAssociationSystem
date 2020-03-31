package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public class RedCard extends OnePlayerEvent {

    public RedCard(int gameTime, Date timeStamp, String description, Player redPlayer) {
        super(gameTime, timeStamp, description, redPlayer);
    }
}
