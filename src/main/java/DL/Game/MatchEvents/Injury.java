package DL.Game.MatchEvents;


import DL.Game.Referee;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     this class represents an injury event
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllInjuryEvents", query = "Select e From Injury e")
})
public class Injury extends OnePlayerEvent {

    public Injury(Referee createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public Injury() {
        super(null,null,0,null);
    }
}
