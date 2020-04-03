package DL.Game.MatchEvents;


import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
/**
 * Description:     this class represents an endGame Event.
 *
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllEndGameEvents", query = "Select e From EndGame e"),
})
public class EndGame extends Event {

    public EndGame(EventUser createdByUser, EventLog eventLog, int gameTime) {
        super(createdByUser, eventLog, gameTime);
    }
}
