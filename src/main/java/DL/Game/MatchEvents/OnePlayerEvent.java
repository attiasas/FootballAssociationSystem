package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.Player;

import javax.persistence.*;

/**
 * Description:     this class represents events of one player
 */
@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "OnePlayersEvents", query = "Select op From OnePlayerEvent op")
})
abstract class OnePlayerEvent extends Event {

    @Column
    private Player player;

    public OnePlayerEvent(Referee createdByUser, EventLog eventLog, int gameTime, Player player) {
        super(createdByUser, eventLog, gameTime);
        this.player = player;
    }

    public OnePlayerEvent() {
        super(null,null,0);
        player = null;
    }
}
