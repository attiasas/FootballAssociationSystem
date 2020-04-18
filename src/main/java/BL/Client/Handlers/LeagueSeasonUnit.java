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
import java.util.List;
import java.util.Map;

/**
 * Description: This class represents the leagueSeasonUnit. it gets the information from the ClientSystem (from the user) and performs tasks as required.
 * responsible of U.C 9.1-9.6 - all the use cases related to League, Season, and Policies.
 **/
public class LeagueSeasonUnit {

    private ClientServerCommunication clientServerCommunication;

    public LeagueSeasonUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    public boolean addNewLeague(String name) {
        if (name != null && !name.equals("")) {
            League newLeague = new League(name);
            return clientServerCommunication.insert(newLeague);
        } else
            return false;
    }

    public boolean addNewSeason(int year) {
        if (year >= 1950) {
            Season newSeason = new Season(year);
            return clientServerCommunication.insert(newSeason);
        } else
            return false;
    }

    public boolean addLeagueSeason(League league, Season season, ScorePolicy scorePolicy, GamePolicy gamePolicy, Date startLeagueDate) {
        if (league != null && season != null && scorePolicy != null && gamePolicy != null) {
            LeagueSeason newLeagueSeason = new LeagueSeason(league, season, gamePolicy, scorePolicy,startLeagueDate);
            return clientServerCommunication.insert(newLeagueSeason);
        } else
            return false;
    }

//    public boolean changeScorePolicy(LeagueSeason leagueSeason, ScorePolicy scorePolicy) {
//        if (leagueSeason != null && scorePolicy != null && leagueSeason.setScorePolicy(scorePolicy)) {
//            return clientServerCommunication.update("UpdateScorePolicy", Map.of("newScorePolicy", scorePolicy, "league",
//                    leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//        } else
//            return false;
//    }

//    public boolean setRefereeInLeagueSeason(LeagueSeason leagueSeason, Referee referee) {
//        if (leagueSeason != null && referee != null && leagueSeason.addReferee(referee)) {
//            return clientServerCommunication.update("UpdateLeagueSeasonRefereesList", Map.of("newReferees", leagueSeason.getReferees(),
//                    "league", leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//        }
//        return false;
//    }

//    public boolean addTeamToLeagueSeason(LeagueSeason leagueSeason, Team team) {
//        if (leagueSeason != null && team != null && leagueSeason.addTeam(team)) {
//            return clientServerCommunication.update("UpdateLeagueSeasonTeamList", Map.of("newTeamList", leagueSeason.getTeamsParticipate(), "league",
//                    leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//        } else
//            return false;
//    }

    public List<League> getLeagues() {
        List<League> leagues = clientServerCommunication.query("GetAllLeagues", null);
        return leagues;
    }

    public List<Season> getSeasons() {
        List<Season> seasons = clientServerCommunication.query("GetAllSeasons", null);
        return seasons;
    }

//    public List<LeagueSeason> getLeagueSeason(Season season) {
//        List<LeagueSeason> leagueSeasons = clientServerCommunication.query("GetAllLeagueSeasons", Map.of("season",season));
//        return leagueSeasons;
//    }

}
