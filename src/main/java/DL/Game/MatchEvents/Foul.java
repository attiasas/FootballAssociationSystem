package DL.Game.MatchEvents;


import javax.persistence.Entity;

/**
 * Description:     this class represents a foul event
 **/
@Entity
public class Foul extends TwoPlayersEvent {

    public Foul(EventUser createdByUser, EventLog eventLog, int gameTime, String injuredPlayer, String foulPlayer) {
        super(createdByUser, eventLog, gameTime, injuredPlayer, foulPlayer);
    }

    public Foul() {
        super(null,null,0,null,null);
    }
}