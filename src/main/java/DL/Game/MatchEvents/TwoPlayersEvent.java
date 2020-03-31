package DL.Game.MatchEvents;

import DL.Team.Members.Player;

import java.util.Date;

public abstract class TwoPlayersEvent extends Event {

    private Player firstPlayer;
    private Player secondPlayer;

    public TwoPlayersEvent(int gameTime, Date timeStamp, String description, Player firstPlayer, Player secondPlayer) {
        super(gameTime, timeStamp, description);
        if (firstPlayer != null && secondPlayer != null){
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        }
    }
}
