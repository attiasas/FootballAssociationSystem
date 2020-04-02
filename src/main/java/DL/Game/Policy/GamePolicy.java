package DL.Game.Policy;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;

import java.util.List;

/**
 * Description:     X
 * ID:              X
 **/
public abstract class GamePolicy {

    private int numberOfRounds;
    private boolean homeAwayMethod;

    public GamePolicy(int numberOfRounds, boolean homeAwayMethod) {
        if (numberOfRounds > 0) {
            this.numberOfRounds = numberOfRounds;
            this.homeAwayMethod = homeAwayMethod;
        }
    }

    public List<Match> matchAssignment(LeagueSeason league) throws IllegalArgumentException {
        if (league == null || (homeAwayMethod == true &&  numberOfRounds <=1))
            throw new IllegalArgumentException("One of the arguments is incorrect");
        return null;
    }

}
