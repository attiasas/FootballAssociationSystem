package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.Player;

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

    public Offside(Referee createdByUser, EventLog eventLog, int gameTime, Player player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public Offside() {
        super(null,null,0,null);
    }
}
