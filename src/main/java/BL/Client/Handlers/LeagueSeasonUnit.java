package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class represents the leagueSeasonUnit.
 * it gets the information from the ClientSystem (from the user) and performs tasks as required.
 * responsible of U.C 9.1-9.6 - all the use cases that related to League, Season, and Policies.
 **/
public class LeagueSeasonUnit {

    private ClientServerCommunication clientServerCommunication;

    /**
     * Ctor with parameters
     *
     * @param clientServerCommunication
     */
    public LeagueSeasonUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * creates new league and adds it into the database. checks if the name is valid - nut null and null empty string.
     *
     * @param name of the league
     * @return true if the league created
     */
    public boolean addNewLeague(String name) {
        if (name != null && !name.equals("")) {
            League newLeague = new League(name);
            return clientServerCommunication.insert(newLeague);
        } else
            return false;
    }

    /**
     * creates new season and adds it into the database. the year should be greater than 1950.
     *
     * @param year of the season
     * @return true if the season created
     */
    public boolean addNewSeason(int year) {
        if (year >= 1950) {
            Season newSeason = new Season(year);
            return clientServerCommunication.insert(newSeason);
        } else
            return false;
    }

    /**
     * Creates new leagueSeason according to the league and season parameters, and adds it into the database.
     * checks that the parameters aren't null.
     *
     * @param league          league of the leagueSeason
     * @param season          season of the league season
     * @param gamePolicy      the game policy according to it the leagueSeason schedule its matches
     * @param scorePolicy     the score policy according to it the leagueSeason calculates its table.
     * @param startLeagueDate the date of the first match.
     * @return true if the league season created
     */
    public boolean addLeagueSeason(League league, Season season, GamePolicy gamePolicy, ScorePolicy scorePolicy, Date startLeagueDate) {
        if (league != null && season != null && scorePolicy != null && gamePolicy != null) {
            LeagueSeason newLeagueSeason = new LeagueSeason(league, season, gamePolicy, scorePolicy, startLeagueDate);
            return clientServerCommunication.insert(newLeagueSeason);
        } else
            return false;
    }

    /**
     * Changes the score policy of a given league season.
     * checks if the parameters aren't null and that the leagueSeason didn't start yet.
     *
     * @param leagueSeason
     * @param scorePolicy
     * @return true if the scorePolicy changed.
     */
    public boolean changeScorePolicy(LeagueSeason leagueSeason, ScorePolicy scorePolicy) {
        if (leagueSeason != null && scorePolicy != null && leagueSeason.setScorePolicy(scorePolicy)) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("newScorePolicy", scorePolicy);
            parameters.put("league", leagueSeason.getLeague());
            parameters.put("season", leagueSeason.getSeason());
            return clientServerCommunication.update("UpdateScorePolicy", parameters);
        } else
            return false;
    }

    /**
     * Sets referee In leagueSeason. checks that the referee and leagueSeason aren't null.
     *
     * @param leagueSeason
     * @param referee
     * @return true if the referee added.
     */
    public boolean setRefereeInLeagueSeason(LeagueSeason leagueSeason, Referee referee) {
        if (leagueSeason != null && referee != null && leagueSeason.addReferee(referee)) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("newReferees", leagueSeason.getReferees());
            parameters.put("league", leagueSeason.getLeague());
            parameters.put("season", leagueSeason.getSeason());
            return clientServerCommunication.update("UpdateLeagueSeasonRefereeList", parameters);
        }
        return false;
    }

    /**
     * Adds Team to leagueSeason. checks that the Team and leagueSeason aren't null,
     * and that the team isn't already exists.
     *
     * @param leagueSeason
     * @param team
     * @return
     */
    public boolean addTeamToLeagueSeason(LeagueSeason leagueSeason, Team team) {
        if (leagueSeason != null && team != null && leagueSeason.addTeam(team)) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("newTeamList", leagueSeason.getTeamsParticipate());
            parameters.put("league", leagueSeason.getLeague());
            parameters.put("season", leagueSeason.getSeason());
            return clientServerCommunication.update("UpdateLeagueSeasonTeamList", parameters);
        } else
            return false;
    }

    /***************Getters*************/

    /**
     * @return the leagues in the system.
     */
    public List<League> getLeagues() {
            List<League> leagues = clientServerCommunication.query("GetAllLeagues", null);
            return leagues;
    }

    /**
     * @return the Seasons in the system.
     */
    public List<Season> getSeasons() {
            List<Season> seasons = clientServerCommunication.query("GetAllSeasons", null);
            return seasons;
    }

    /**
     * @return the teams in the system.
     */
    public List<Team> getTeams() {
            List<Team> teams = clientServerCommunication.query("Team", null);
            return teams;
    }

    /**
     * @return the LeagueSeasons in the system.
     */
    public List<LeagueSeason> getLeagueSeasons(Season season) {
            if (season != null) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("season", season);
                List<LeagueSeason> leagueSeasons = clientServerCommunication.query("GetAllLeagueSeasons", parameters);
                return leagueSeasons;
            }
            return null;
    }


    /**
     * TODO: ADD TESTS FOR THOSE FUNCTION
     * /**
     *
     * @return the specific season
     */
    public Season getSeason(int year) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("year", year);
            List<Season> requiredSeason = clientServerCommunication.query("GetSeason", parameters);
            if (requiredSeason.size() > 0) {
                return requiredSeason.get(0);
            }
            return null;
    }


    public LeagueSeason getLeagueSeason(Season season, League league) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("season", season);
            parameters.put("league", league);
            List<LeagueSeason> requiredLeagueSeason = clientServerCommunication.query("GetLeagueSeason", parameters);
            if (requiredLeagueSeason.size() > 0) {
                return requiredLeagueSeason.get(0);
            }
            return null;

    }
}
