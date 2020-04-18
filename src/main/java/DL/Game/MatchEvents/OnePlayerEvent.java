package DL.Game.MatchEvents;

import DL.Game.Referee;

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
    private String player;

    public OnePlayerEvent(Referee createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime);
        this.player = player;
    }

    public OnePlayerEvent() {
        super(null,null,0);
        player = null;
    }
}
