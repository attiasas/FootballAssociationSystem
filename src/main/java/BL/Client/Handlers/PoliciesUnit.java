package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Team.Team;

import java.util.List;
import java.util.Map;


public class PoliciesUnit {

    private ClientServerCommunication clientServerCommunication;

    public PoliciesUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

//    public void addNewGamePolicy(int numberOfRounds, int gamesPerDay) {
//        if (numberOfRounds > 0 && gamesPerDay > 0) {
//            List gamesPolicy = clientServerCommunication.query("getGamePolicy", Map.of("numberOfRounds", numberOfRounds, "gamesPerDay", gamesPerDay));
//            if (gamesPolicy == null || gamesPolicy.size() == 0) {
//                GamePolicy newGamePolicy = new GamePolicy(numberOfRounds, gamesPerDay);
//                clientServerCommunication.insert(newGamePolicy);
//            }
//        }
//    }

//    public void addNewScorePolicy(int winPoints, int drawPoints, int losePoints) {
//        List scorePolicy = clientServerCommunication.query("GetScorePolicy", Map.of("winPoints", winPoints, "drawPoints", drawPoints, "losePoints", losePoints));
//        if (scorePolicy == null || scorePolicy.size() == 0) {
//            ScorePolicy newScorePolicy = new ScorePolicy(winPoints, drawPoints, losePoints);
//            clientServerCommunication.insert(newScorePolicy);
//        }
//    }

//    //TODO: update the teams in the database if needed
//    public void assignMatches(LeagueSeason leagueSeason) {
//        if (leagueSeason != null) {
//            leagueSeason.scheduleLeagueMatches();
//            clientServerCommunication.update("UpdateLeagueSeasonMatchList", Map.of("matchList", leagueSeason.getMatches(), "league",
//                    leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//        }
//    }

    public List<Map.Entry<Team, Integer[]>> calculateLeagueTable(LeagueSeason leagueSeason) {
        if (leagueSeason != null ) {
            return leagueSeason.getLeagueSeasonTable();
        }
        return null;
    }

    //TODO: update referees list in database if needed
//    public void setRefereeInMatches(LeagueSeason leagueSeason) {
//        if (leagueSeason != null) {
//            leagueSeason.setRefereesInMatches();
//            clientServerCommunication.update("UpdateLeagueSeasonMatchList", Map.of("matchList", leagueSeason.getMatches(), "league",
//                    leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//            clientServerCommunication.update("UpdateLeagueSeasonRefereeList", Map.of("refereesList", leagueSeason.getReferees(), "league",
//                    leagueSeason.getLeague(), "season", leagueSeason.getSeason()));
//        }
//    }


}
