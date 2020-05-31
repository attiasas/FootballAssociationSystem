package BL.Communication;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import DL.Users.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicationLeagueSeasonAndPoliciesStub extends ClientServerCommunication {

    private List<GamePolicy> gamePolicies;
    private List<ScorePolicy> scorePolicies;
    private List<LeagueSeason> leagueSeasons;
    private List<League> leagues;
    private List<Season> seasons;
    private List<User> users;


    public CommunicationLeagueSeasonAndPoliciesStub() {
        this.users = new ArrayList<>();
        this.scorePolicies = new ArrayList<>();
        this.gamePolicies = new ArrayList<>();
        this.leagueSeasons = new ArrayList<>();
        this.leagues = new ArrayList<>();
        this.seasons = new ArrayList<>();
    }

    public CommunicationLeagueSeasonAndPoliciesStub(List<User> users) {
        this();
        this.users = users;
        System.out.println("regular");
    }

    public void addLeague(League l){
        leagues.add(l);
    }
    public void addSeason(Season s){
        seasons.add(s);
    }
    public void addLeagueSeason(LeagueSeason ls){
        leagueSeasons.add(ls);
    }
    public void addGamePolicy(GamePolicy gp){
        gamePolicies.add(gp);
    }
    public void addScorePolicy(ScorePolicy sp){
        scorePolicies.add(sp);
    }

    @Override
    public List login(String username, String password) {
        List<User> user = new ArrayList<>();
        for (User u : users){
            if (u.getUsername().equals(username) && u.getHashedPassword().equals(password)){
                user.add(u);
            }
        }
        return user;
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters) {
        switch (queryName) {
            case "GetScorePolicy":
                List<ScorePolicy> spList = new ArrayList<>();
                for (ScorePolicy sp : scorePolicies) {
                    if (sp.getDrawPoints() == (int) parameters.get("drawPoints") &&
                            sp.getWinPoints() == (int) parameters.get("winPoints") &&
                            sp.getLosePoints() == (int) parameters.get("losePoints")) {
                        spList.add(sp);
                        break;
                    }
                }
                return spList;
            case "getGamePolicy":
                List<GamePolicy> gpList = new ArrayList<>();
                for (GamePolicy gp : gamePolicies) {
                    if (gp.getNumberOfRounds() == (int) parameters.get("numberOfRounds") &&
                            gp.getGamesPerDay() == (int) parameters.get("gamesPerDay"))
                        gpList.add(gp);
                    break;
                }
                return gpList;
            case "GetScorePolicies":
                return scorePolicies;
            case "GetGamePolicies":
                return gamePolicies;
            case "GetAllLeagues":
                return leagues;
            case "GetAllSeasons":
                return seasons;
            case "GetAllLeagueSeasons":
                List<LeagueSeason> leagueSeasonsList = new ArrayList<>();
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getSeason().equals(parameters.get("season"))) {
                        leagueSeasonsList.add(ls);
                    }
                }
                return leagueSeasonsList;
            case "GetLeague":
                List<League> leagueList = new ArrayList<>();
                for (League l : leagues) {
                    if (l.getName().equals(parameters.get("name"))) {
                        leagueList.add(l);
                        return leagueList;
                    }
                }
                return leagueList;
            case "GetSeason":
                List<Season> seasonList = new ArrayList<>();
                for (Season s : seasons) {
                    if (s.getYear()== (Integer) parameters.get("year")) {
                        seasonList.add(s);
                        return seasonList;
                    }
                }
                return seasonList;
            case "GetLeagueSeason":
                List<LeagueSeason> leagueSeasonsL = new ArrayList<>();
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getSeason().getYear() == ((Season) parameters.get("season")).getYear()
                            && ls.getLeague().getName().equals(((League) parameters.get("league")).getName())) {
                        leagueSeasonsL.add(ls);
                        return leagueSeasonsL;
                    }
                }
                return leagueSeasonsL;
            default:
                return null;
        }
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters) {
        League league;
        Season season;
        switch (queryName) {
            case "UpdateLeagueSeasonMatchList":
                List<Match> matchList = (List<Match>) parameters.get("matchList");
                league = (League) parameters.get("league");
                season = (Season) parameters.get("season");
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getSeason().equals(season) && ls.getLeague().equals(league)) {
                        if (matchList.size() > 0) {
                            return true;
                        }
                    }
                }
                return false;
            case "UpdateLeagueSeasonRefereeList":
                List<Referee> referees = (List<Referee>) parameters.get("newReferees");
                league = (League) parameters.get("league");
                season = (Season) parameters.get("season");
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getSeason().equals(season) && ls.getLeague().equals(league)) {
                        if (referees.size() > 0) {
                            return true;
                        }
                    }
                }
                return false;
            case "UpdateLeagueSeasonTeamList":
                List<Team> teamList = (List<Team>) parameters.get("newTeamList");
                league = (League) parameters.get("league");
                season = (Season) parameters.get("season");
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getSeason().equals(season) && ls.getLeague().equals(league)) {
                        if (teamList.size() > 0) {
                            return true;
                        }
                    }
                }
                return false;
            case "UpdateScorePolicy":
                int winPoints =  (Integer) parameters.get("winPoints");
                int drawPoints =  (Integer) parameters.get("drawPoints");
                int losePoints =  (Integer) parameters.get("losePoints");
                int leagueSeasonID =  (Integer) parameters.get("leagueSeasonID");
                for (LeagueSeason ls : leagueSeasons) {
                    if (ls.getLeagueSeasonID() == leagueSeasonID) {
                        ScorePolicy scorePolicy = new ScorePolicy(winPoints,drawPoints,losePoints);
                        return ls.setScorePolicy(scorePolicy);
                    }
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean insert(Object toInsert) {
        if (toInsert instanceof ScorePolicy) {
            if (checkIfObjectExists(toInsert, scorePolicies) == -1) {
                scorePolicies.add((ScorePolicy) toInsert);
                return true;
            }
        } else if (toInsert instanceof GamePolicy) {
            if (checkIfObjectExists(toInsert, gamePolicies) == -1) {
                gamePolicies.add((GamePolicy) toInsert);
                return true;
            }
        } else if (toInsert instanceof League) {
            if (checkIfObjectExists(toInsert, leagues) == -1) {
                leagues.add((League) toInsert);
                return true;
            }
        } else if (toInsert instanceof Season) {
            if (checkIfObjectExists(toInsert, seasons) == -1) {
                seasons.add((Season) toInsert);
                return true;
            }
        } else if (toInsert instanceof LeagueSeason) {
            if (checkIfObjectExists(toInsert, leagueSeasons) == -1) {
                leagueSeasons.add((LeagueSeason) toInsert);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Object toDelete) {
        int index = -1;
        if (toDelete instanceof League) {
            if ((index = checkIfObjectExists(toDelete, leagues)) > -1) {
                leagues.remove(index);
                return true;
            }
        } else if (toDelete instanceof Season) {
            if ((index = checkIfObjectExists(toDelete, seasons)) > -1) {
                seasons.remove(index);
                return true;
            }
        } else if (toDelete instanceof LeagueSeason) {
            if ((index = checkIfObjectExists(toDelete, leagueSeasons)) > -1) {
                leagueSeasons.remove(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean merge(Object toMerge){
        if (toMerge instanceof Team){
            Team t = (Team) toMerge;
            for (LeagueSeason s : t.getLeagueSeasons()){
                if (!s.getTeamsParticipate().contains(t))
                    s.addTeam(t);
            }
        } else if (toMerge instanceof Referee){
            Referee r = (Referee) toMerge;
            for (LeagueSeason s : r.getLeagueSeasons()){
                if (!s.getReferees().contains(r))
                    s.addReferee(r);
            }
        } else if (toMerge instanceof LeagueSeason){
            LeagueSeason ls = (LeagueSeason) toMerge;
            for (LeagueSeason s : leagueSeasons){
                if (s.equals(ls))
                    s.setGames(ls.getMatches());
            }
        }
        return true;
    }

    @Override
    public boolean transaction(List<SystemRequest> systemRequests){
        return true;
    }

    @Override
    public boolean startNotificationListener() {
        return true;
    }

    /**
     * checks if two objects are the same and returns their index
     *
     * @param toCheck    object to find in the list
     * @param objectList list of objects
     * @return the required object index
     */
    private int checkIfObjectExists(Object toCheck, List objectList) {
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i).equals(toCheck))
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "CommunicationLeagueSeasonAndPoliciesStub{}";
    }
}
