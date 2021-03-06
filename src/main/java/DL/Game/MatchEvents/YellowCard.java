package DL.Game.MatchEvents;


import DL.Game.Referee;
import DL.Team.Members.Player;

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

    public YellowCard(Referee createdByUser, EventLog eventLog, int gameTime, Player player) {
        super(createdByUser, eventLog, gameTime, player);
    }

    public YellowCard() {
        super(null, null, 0,null);
    }

    @Override
    public String getType() { return Type(); }

    public static String Type() { return "Yellow Card"; }

    @Override
    public String toString() {
        return getPlayer() + " Got A Yellow Card";
    }
}
