package DL.Game.MatchEvents;

import javax.persistence.*;

/**
 * Description:     this class represents events of one player
 */
@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "OnePlayersEvents", query = "Select e From OnePlayerEvent e")
})
abstract class OnePlayerEvent extends Event {

    @Column
    private String player;

    public OnePlayerEvent(EventUser createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime);
        this.player = player;
    }

    public OnePlayerEvent() {
        super(null,null,0);
        player = null;
    }
}
