package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

abstract class OnePlayerEvent extends Event {

    private Player player;

    public OnePlayerEvent(int gameTime, Date timeStamp, String description, Player player) {
        super(gameTime, timeStamp, description);
        if (player != null)
            this.player = player;
    }
}
