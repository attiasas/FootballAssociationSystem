package DL.Game.MatchEvents;


import DL.Game.Referee;

import javax.persistence.DiscriminatorValue;
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
@DiscriminatorValue(value = "EndGame")
public class EndGame extends Event {

    public EndGame(Referee createdByUser, EventLog eventLog, int gameTime) {
        super(createdByUser, eventLog, gameTime);
    }

    public EndGame(){ this(null,null,-1); }

    public static String Type() { return "End Game"; }

    @Override
    public String getType() { return Type(); }

    @Override
    public String toString() {
        return "Match Ended";
    }
}
