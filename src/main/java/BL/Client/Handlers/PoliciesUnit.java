package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Team.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: This class represents the PoliciesUnit.
 * it gets the information from the ClientSystem (from the user) and performs tasks as required.
 * responsible of creating policies and run those policies - schedule matches, calculate league table, set referees in matches.
 **/

public class PoliciesUnit {

    private ClientServerCommunication clientServerCommunication;

    /**
     * Ctor with parameters.
     * @param clientServerCommunication responsible for the connection to the database.
     */
    public PoliciesUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * Creates new GamePolicy and adds it into the database.
     * checks that the gamePolicy isn't exist.
     * @param numberOfRounds should be greater than 0;
     * @param gamesPerDay should be greater than 0;
     * @return true if the game policy created
     */
    public boolean addNewGamePolicy(int numberOfRounds, int gamesPerDay) {
        if (numberOfRounds > 0 && gamesPerDay > 0) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("numberOfRounds", numberOfRounds);
            parameters.put("gamesPerDay", gamesPerDay);
            List gamesPolicy = clientServerCommunication.query("getGamePolicy", parameters);
            if (gamesPolicy == null || gamesPolicy.size() == 0) {
                GamePolicy newGamePolicy = new GamePolicy(numberOfRounds, gamesPerDay);
                return clientServerCommunication.insert(newGamePolicy);
            }
        }
        return false;
    }

    /**
     * Creates new scorePolicy and adds it into the database.
     * checks that the scorePolicy isn't exist.
     * @param winPoints points the team get for winning.
     * @param drawPoints points the team get for draw.
     * @param losePoints points the team get for losing.
     * @return true if the score policy created
     */
    public boolean addNewScorePolicy(int winPoints, int drawPoints, int losePoints) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("winPoints", winPoints);
        parameters.put("drawPoints", drawPoints);
        parameters.put("losePoints", losePoints);
        List scorePolicy = clientServerCommunication.query("GetScorePolicy", parameters);
        if (scorePolicy == null || scorePolicy.size() == 0) {
            ScorePolicy newScorePolicy = new ScorePolicy(winPoints, drawPoints, losePoints);
            return clientServerCommunication.insert(newScorePolicy);
        }
        return false;
    }

    /**
     * Schedule matches in the given leagueSeason.
     * checks that the league season isn't null, and that the league season isn't have
     * matches yet (this policy can execute only one time of a league season).
     * @param leagueSeason
     * @return
     */
    //TODO: update the teams in the database if needed
    public boolean scheduleMatches(LeagueSeason leagueSeason) {
        if (leagueSeason != null) {
            if (leagueSeason.getMatches().size() == 0) {
                leagueSeason.scheduleLeagueMatches();
                if (leagueSeason.getMatches().size() > 0) {
                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("matchList", leagueSeason.getMatches());
                    parameters.put("league", leagueSeason.getLeague());
                    parameters.put("season", leagueSeason.getSeason());
                    return clientServerCommunication.update("UpdateLeagueSeasonMatchList", parameters);
                }
            }
        }
        return false;
    }

    /**
     * Calculates the league table.
     * checks that the leagueSeason isn't null.
     * @param leagueSeason
     * @return the league table sorted by place.
     */
    public List<Map.Entry<Team, Integer[]>> calculateLeagueTable(LeagueSeason leagueSeason) {
        if (leagueSeason != null) {
            return leagueSeason.getLeagueSeasonTable();
        }
        return null;
    }

    /**
     * Sets referee in every match of the given leagueSeason.
     * @param leagueSeason
     * @return true if the assertion succeeded.
     */
    //TODO: update referees list in database if needed
    public boolean setRefereeInMatches(LeagueSeason leagueSeason) {
        if (leagueSeason != null && leagueSeason.getMatches().size() > 0) {
            leagueSeason.setRefereesInMatches();

            HashMap<String, Object> firstParameters = new HashMap<>();
            firstParameters.put("matchList", leagueSeason.getMatches());
            firstParameters.put("league", leagueSeason.getLeague());
            firstParameters.put("season", leagueSeason.getSeason());

            HashMap<String, Object> secondParameters = new HashMap<>();
            secondParameters.put("newReferees", leagueSeason.getReferees());
            secondParameters.put("league", leagueSeason.getLeague());
            secondParameters.put("season", leagueSeason.getSeason());

            return clientServerCommunication.update("UpdateLeagueSeasonRefereeList", secondParameters)
                    && clientServerCommunication.update("UpdateLeagueSeasonMatchList", firstParameters);
        }
        return false;
    }

    /**************Getters*******************/

    /**
     * @return all game policies in the system
     */
    public List<GamePolicy> getGamePolicies() {
        List<GamePolicy> gamePolicies = clientServerCommunication.query("GetGamePolicies", null);
        return gamePolicies;
    }

    /**
     * @return all score policies in the system
     */
    public List<ScorePolicy> getScorePolicies() {
        List<ScorePolicy> scorPolicies = clientServerCommunication.query("GetScorePolicies", null);
        return scorPolicies;
    }

}
