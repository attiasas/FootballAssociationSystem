package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

abstract class OnePlayerEvent extends Event {

    private Player player;

    public OnePlayerEvent(int gameTime, String description, Player player) {
        super(gameTime, description);
        if (player != null)
            this.player = player;
    }
}
