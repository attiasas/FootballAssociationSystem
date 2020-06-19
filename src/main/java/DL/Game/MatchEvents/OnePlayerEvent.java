package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.Player;

import javax.persistence.*;

/**
 * Description:     this class represents events of one player
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "OnePlayersEvents", query = "Select op From OnePlayerEvent op")
})
@DiscriminatorValue(value = "OnePlayerEvent")
public abstract class OnePlayerEvent extends Event {

    @ManyToOne
    private Player player;

    public OnePlayerEvent(Referee createdByUser, EventLog eventLog, int gameTime, Player player) {
        super(createdByUser, eventLog, gameTime);
        this.player = player;
    }

    public OnePlayerEvent() {
        super(null,null,0);
        player = null;
    }

    public Player getPlayer() {
        return player;
    }
}
