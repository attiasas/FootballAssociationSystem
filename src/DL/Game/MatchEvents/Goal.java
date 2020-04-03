package DL.Game.MatchEvents;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     this class represents a goal event
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllGoalEvents", query = "Select e From Goal e")
})
public class Goal extends OnePlayerEvent {

    public Goal(EventUser createdByUser, EventLog eventLog, int gameTime, String player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public Goal() {
        super(null,null,0,null);
    }
}
