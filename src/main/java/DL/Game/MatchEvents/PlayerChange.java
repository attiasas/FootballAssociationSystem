package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public class PlayerChange extends TwoPlayersEvent {

    public PlayerChange(int gameTime, String description, Player inPlayer, Player outPlayer) {
        super(gameTime, description, inPlayer, outPlayer);
    }
}
