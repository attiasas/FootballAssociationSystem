package DL.Game.MatchEvents;

import DL.Game.Referee;
import DL.Team.Members.Player;

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

    public Goal(Referee createdByUser, EventLog eventLog, int gameTime, Player player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public Goal() {
        super(null,null,0,null);
    }
}
