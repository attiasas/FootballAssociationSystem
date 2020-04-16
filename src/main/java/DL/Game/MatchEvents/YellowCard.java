package DL.Game.MatchEvents;


import DL.Game.Referee;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     this class represents a YellowCard event
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllYellowCardEvents", query = "Select e From YellowCard e")
})
public class YellowCard extends OnePlayerEvent {

    public YellowCard(Referee createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public YellowCard() {
        super(null, null, 0,null);
    }
}
