package DL.Game.MatchEvents;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     this class represents an offside event
 * ID:              X
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllOffsideEvents", query = "Select e From Offside e")
})
public class Offside extends OnePlayerEvent {

    public Offside(EventUser createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public Offside() {
        super(null,null,0,null);
    }
}
