package DL.Game.Policy;


/**
 * Description:     This class represents a Score Policy
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

    public boolean calculateLeagueTable(){
        return false;
    }

}
