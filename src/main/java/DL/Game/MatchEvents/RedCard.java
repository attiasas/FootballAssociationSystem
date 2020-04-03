package DL.Game.MatchEvents;


import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     This class represents a redCard event
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllRedCardEvents", query = "Select e From RedCard e")
})
public class RedCard extends OnePlayerEvent {

    public RedCard(EventUser createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public RedCard() {
        super(null,null,0,null);
    }
}
