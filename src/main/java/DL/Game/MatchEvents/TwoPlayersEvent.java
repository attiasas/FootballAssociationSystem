package DL.Game.MatchEvents;

import DL.Game.Referee;

import javax.persistence.*;

/**
 * Description:     this abstract class represents events of two players
 *
 */


@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "TwoPlayersEvents", query = "Select tp From TwoPlayersEvent tp")
})

@IdClass(Event.EntryPK.class)
public abstract class TwoPlayersEvent extends Event {

    @Column
    private String firstPlayer;
    @Column
    private String secondPlayer;

    public TwoPlayersEvent(Referee createdByUser, EventLog eventLog, int gameTime, String firstPlayer, String secondPlayer) {
        super(createdByUser, eventLog, gameTime);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public TwoPlayersEvent() {
        super(null,null,0);
        firstPlayer = secondPlayer = null;
    }
}
