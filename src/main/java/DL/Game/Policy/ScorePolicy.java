package DL.Game.Policy;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Team;

import javax.persistence.*;
import java.util.*;

/**
 * Description:     This class represents a Score Policy
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetScorePolicies", query = "SELECT sp From ScorePolicy sp"),
        @NamedQuery(name = "GetScorePolicy", query = "SELECT sp FROM ScorePolicy sp WHERE sp.winPoints =: winPoints AND sp.drawPoints =: drawPoints AND sp.losePoints=:losePoints")

})
@IdClass(ScorePolicy.EntryPK.class)
public class ScorePolicy {

    /**
     * For Composite Primary Key
     */
    public class EntryPK {
        public int winPoints;
        public int drawPoints;
        public int losePoints;
    }

    @Column
    private int winPoints;
    @Column
    private int drawPoints;
    @Column
    private int losePoints;


    /**
     * Ctor
     * @param winPoints
     * @param drawPoints
     * @param losePoints
     */
    public ScorePolicy(int winPoints, int drawPoints, int losePoints) {
        this.winPoints = winPoints;
        this.drawPoints = drawPoints;
        this.losePoints = losePoints;
    }

    /**
     * Default ctor
     */
    public ScorePolicy() {
        this(3, 1, 0);
    }

    public int getWinPoints() {
        return winPoints;
    }

    public int getDrawPoints() {
        return drawPoints;
    }

    public int getLosePoints() {
        return losePoints;
    }

    /**
     * Caculates the league table.
     * First runs over the matches and updates the team points and goal difference according to the match score.
     * than sorts the list according to the team points - if the points equal, checks the goal difference.
     * @param leagueSeason
     * @return sorted list of team and their points and goals - leagueTable
     */
    public List<Map.Entry<Team, Integer[]>> calculateLeagueTable(LeagueSeason leagueSeason) {
        HashMap<Team, Integer[]> leagueTable = new HashMap<>();
        if (leagueSeason != null && leagueSeason.getMatches().size() > 0) {
            for (Match match : leagueSeason.getMatches()) {
                //if the home team wins
                if (match.getHomeScore() > match.getAwayScore()) {
                    updateTeamPointsInTable(leagueTable, match.getHomeTeam(), this.winPoints);
                    updateTeamGoalsInTable(leagueTable, match.getHomeTeam(), match.getHomeScore() - match.getAwayScore()); //difference between goals
                    updateTeamPointsInTable(leagueTable, match.getAwayTeam(), this.losePoints);
                    updateTeamGoalsInTable(leagueTable, match.getAwayTeam(), match.getAwayScore() - match.getHomeScore());
                }
                // no need of goal difference because the draw score
                else if (match.getHomeScore() == match.getAwayScore()) {
                    updateTeamPointsInTable(leagueTable, match.getHomeTeam(), this.drawPoints);
                    updateTeamPointsInTable(leagueTable, match.getAwayTeam(), this.drawPoints);
                }
                //if the away team wins
                else {
                    updateTeamPointsInTable(leagueTable, match.getHomeTeam(), this.losePoints);
                    updateTeamGoalsInTable(leagueTable, match.getHomeTeam(), match.getHomeScore() - match.getAwayScore()); //difference between goals
                    updateTeamPointsInTable(leagueTable, match.getAwayTeam(), this.winPoints);
                    updateTeamGoalsInTable(leagueTable, match.getAwayTeam(), match.getAwayScore() - match.getHomeScore());
                }
            }
            List<Map.Entry<Team, Integer[]>> sortedLeagueTable = new ArrayList<>(leagueTable.entrySet());
            Collections.sort(sortedLeagueTable, new compareTeams());
            return sortedLeagueTable;
        }
        return null;
    }

    /**
     * Adds points to the required team according to the scorePolicy
     *
     * @param leagueTable
     * @param teamToUpdate
     * @param pointsToAdd
     */
    private void updateTeamPointsInTable(HashMap<Team, Integer[]> leagueTable, Team teamToUpdate, int pointsToAdd) {
        Integer[] newTeamInfo;
        if (leagueTable != null && teamToUpdate != null) {
            if (leagueTable.containsKey(teamToUpdate)) {
                newTeamInfo = leagueTable.get(teamToUpdate);
                newTeamInfo[0] += pointsToAdd;
                leagueTable.replace(teamToUpdate, newTeamInfo);
            } else {
                newTeamInfo = new Integer[]{pointsToAdd, 0};
                leagueTable.put(teamToUpdate, newTeamInfo);
            }
        }
    }

    /**
     * Adds goals to the required team according to the match score
     *
     * @param leagueTable
     * @param teamToUpdate
     * @param pointsToAdd
     */
    private void updateTeamGoalsInTable(HashMap<Team, Integer[]> leagueTable, Team teamToUpdate, int goalsToAdd) {
        Integer[] newTeamInfo;
        if (leagueTable != null && teamToUpdate != null) {
            if (leagueTable.containsKey(teamToUpdate)) {
                newTeamInfo = leagueTable.get(teamToUpdate);
                newTeamInfo[1] += goalsToAdd;
                leagueTable.replace(teamToUpdate, newTeamInfo);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScorePolicy that = (ScorePolicy) o;
        return winPoints == that.winPoints &&
                drawPoints == that.drawPoints &&
                losePoints == that.losePoints;
    }

    @Override
    public int hashCode() {
        return Objects.hash(winPoints, drawPoints, losePoints);
    }


    /**
     * This nested class compares between two Teams according to their points. if ths they have equal points, it compares them according to the goal difference.
     */
    private class compareTeams implements Comparator<Map.Entry<Team, Integer[]>> {

        @Override
        public int compare(Map.Entry<Team, Integer[]> o1, Map.Entry<Team, Integer[]> o2) {
            //compare points
            if (o1.getValue()[0] > o2.getValue()[0]) {
                return -1;
            } else if (o1.getValue()[0] < o2.getValue()[0]) {
                return 1;
            }
            //if points are equals compare goals difference
            else if (o1.getValue()[1] > o2.getValue()[1]) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
