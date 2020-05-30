package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.SystemRequest;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Team.Team;
import org.hibernate.Hibernate;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
     *
     * @param clientServerCommunication responsible for the connection to the database.
     */
    public PoliciesUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * Creates new GamePolicy and adds it into the database.
     * checks that the gamePolicy isn't exist.
     *
     * @param numberOfRounds should be greater than 0;
     * @param gamesPerDay    should be greater than 0;
     * @return true if the game policy created
     */
    public boolean addNewGamePolicy(int numberOfRounds, int gamesPerDay) throws Exception {
        if (numberOfRounds > 0 && gamesPerDay > 0) {

            if (gamesPerDay > 7)
                throw new Exception("The maximum games per day is 7.");

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("numberOfRounds", numberOfRounds);
            parameters.put("gamesPerDay", gamesPerDay);
            List gamesPolicy = clientServerCommunication.query("getGamePolicy", parameters);

            if (gamesPolicy != null && gamesPolicy.size() == 0) {
                GamePolicy newGamePolicy = new GamePolicy(numberOfRounds, gamesPerDay);
                return clientServerCommunication.insert(newGamePolicy);

                //error with the server
            } else if (gamesPolicy == null) {
                throw new Exception("There was a problem with the connection to the server. Please try again later");

                //game policy exists
            } else {
                throw new Exception("Game policy already exists.");
            }
            //error with parameters
        } else {
            throw new Exception("Parameters must be greater than 0.");
        }
    }

    /**
     * Creates new scorePolicy and adds it into the database.
     * checks that the scorePolicy isn't exist.
     *
     * @param winPoints  points the team get for winning.
     * @param drawPoints points the team get for draw.
     * @param losePoints points the team get for losing.
     * @return true if the score policy created
     */
    public boolean addNewScorePolicy(int winPoints, int drawPoints, int losePoints) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("winPoints", winPoints);
        parameters.put("drawPoints", drawPoints);
        parameters.put("losePoints", losePoints);
        List scorePolicy = clientServerCommunication.query("GetScorePolicy", parameters);
        if (scorePolicy != null && scorePolicy.size() == 0) {
            ScorePolicy newScorePolicy = new ScorePolicy(winPoints, drawPoints, losePoints);
            return clientServerCommunication.insert(newScorePolicy);

            //error with the server
        } else if (scorePolicy == null) {
            throw new Exception("There was a problem with the connection to the server. Please try again later");

            //score policy exists
        } else {
            throw new Exception("Score policy already exists.");
        }
    }

    /**
     * Schedule matches in the given leagueSeason.
     * checks that the league season isn't null, and that the league season isn't have
     * matches yet (this policy can execute only one time of a league season).
     *
     * @param leagueSeason
     * @return
     */
    public boolean scheduleMatches(LeagueSeason leagueSeason) throws Exception {
        if (leagueSeason != null) {
            if (leagueSeason.getMatches().size() == 0) {
                //League has one team
                if (!leagueSeason.scheduleLeagueMatches())
                    throw new Exception("There is less than two teams in the leagueSeason. " +
                            "Please add teams first and than try again.");
                //league has matches - send transaction of insert matches
                List<SystemRequest> systemRequests = new ArrayList<>();
                for (Match m : leagueSeason.getMatches()) {
                    systemRequests.add(new SystemRequest(SystemRequest.Type.Insert, null, m));
                }
                return clientServerCommunication.transaction(systemRequests);
            }
            //LeagueSeason already have matches
            else {
                throw new Exception("Matches for this league already scheduled.");
            }
        } else {
            throw new Exception("LeagueSeason can not be empty. Please choose leagueSeason.");
        }
    }

    /**
     * Calculates the league table.
     * checks that the leagueSeason isn't null.
     *
     * @param leagueSeason
     * @return the league table sorted by place.
     */
    public List<Map.Entry<Team, Integer[]>> calculateLeagueTable(LeagueSeason leagueSeason) throws Exception {
        if (leagueSeason != null) {
            return leagueSeason.getLeagueSeasonTable();
        }
        throw new Exception("LeagueSeason can not be empty. Please choose leagueSeason.");

    }

    /**
     * Sets referee in every match of the given leagueSeason.
     *
     * @param leagueSeason
     * @return true if the assertion succeeded.
     */
    //TODO: update referees list in database if needed
    public boolean setRefereeInMatches(LeagueSeason leagueSeason) throws Exception {

        if (leagueSeason == null)
            throw new Exception("LeagueSeason can not be empty. Please choose leagueSeason.");

        if (leagueSeason.getMatches() == null || leagueSeason.getMatches().size() == 0)
            throw new Exception("LeagueSeason doesn't have matches. Please schedule matches for this leagueSeason first.");

        if (!leagueSeason.setRefereesInMatches())
            throw new Exception("LeagueSeason doesn't have enough referees. Please add more referees to the leagueSeason.");

//        HashMap<String, Object> firstParameters = new HashMap<>();
//        firstParameters.put("matchList", leagueSeason.getMatches());
//        firstParameters.put("league", leagueSeason.getLeague());
//        firstParameters.put("season", leagueSeason.getSeason());
//
//        HashMap<String, Object> secondParameters = new HashMap<>();
//        secondParameters.put("newReferees", leagueSeason.getReferees());
//        secondParameters.put("league", leagueSeason.getLeague());
//        secondParameters.put("season", leagueSeason.getSeason());
//
//        return clientServerCommunication.update("UpdateLeagueSeasonRefereeList", secondParameters)
//                && clientServerCommunication.update("UpdateLeagueSeasonMatchList", firstParameters);

        return clientServerCommunication.merge(leagueSeason);
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
