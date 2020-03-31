package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public class Offside extends OnePlayerEvent {

    public Offside(int gameTime, Date timeStamp, String description, Player offsidePlayer) {
        super(gameTime, timeStamp, description, offsidePlayer);
    }
}
