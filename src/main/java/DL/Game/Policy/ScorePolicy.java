package DL.Game.Policy;

import DL.Game.LeagueSeason.League;
import DL.Game.Match;

import java.util.List;

/**
 * Description:     X
 * ID:              X
 **/
public class ScorePolicy
{

    private int winPoints;
    private int drawPoints;
    private int losePoints;
    private boolean goalDifferenceFactor;
    private boolean lastGamesScoreFactor;

    public ScorePolicy(int winPoints, int drawPoints, int losePoints,boolean goalDifferenceFactor,boolean lastGamesScoreFactor) {
        this.winPoints = winPoints;
        this.drawPoints = drawPoints;
        this.losePoints = losePoints;
        this.goalDifferenceFactor = goalDifferenceFactor;
        this.lastGamesScoreFactor = lastGamesScoreFactor;
    }

    public boolean calculateLeagueTable(List<Match> matchList, League league){
        return false;
    }

}
