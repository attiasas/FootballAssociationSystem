package BL.Communication;

import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamUser;
import DL.Team.Team;

import javax.persistence.ManyToOne;
import java.util.*;

public class CommunicationTeamAssetStub extends ClientServerCommunication {

    private List<Team> teams;
    private List<Stadium> stadiums;
    private List<Player> players;
    private List<Coach> coaches;
    private List<TeamUser> teamUsers;
    private List<String> queryToList;

    public CommunicationTeamAssetStub(List<Team> teams, List<Stadium> stadiums, List<Player> players, List<Coach> coaches, List<TeamUser> teamUsers) {

        this.teams = teams;
        this.stadiums = stadiums;
        this.players = players;
        this.coaches = coaches;
        this.teamUsers = teamUsers;

    }

    public CommunicationTeamAssetStub() {

        teams = new ArrayList<>();
        stadiums = new ArrayList<>();
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        teamUsers = new ArrayList<>();

    }

    private void QueriesInUse() {

        queryToList = new ArrayList<>();
        queryToList.add("stadiumByName"); //query = SELECT s FROM Stadium s WHERE s.name = :name
        queryToList.add("stadiumsByTeam"); //query = SELECT s FROM Stadium s WHERE :team IN (s.teams) AND s.team.close = false
        queryToList.add("setStadiumDetails"); //query = UPDATE Stadium s SET s.name = :newName, s.capacity = :newCapacity WHERE s.team = :team
        queryToList.add("playersByTeam"); //query = SELECT p FROM Player p WHERE p.team = :team AND p.team.close = false AND p.active = true
        queryToList.add("coachesByTeam"); //query = SELECT c FROM Coach c WHERE c.team = :team AND c.active = true AND c.team.close = false
        queryToList.add("updatePlayerDetails"); //query = UPDATE Player p SET p.name = :name, p.role = :role, p.team = :team, p.active = :active, p.birthDate = :birthDate WHERE p.fan = :fan
        queryToList.add("updateCoachDetails"); //query = UPDATE Coach c SET c.name = :name, c.role = :role, c.team = :team, c.active = :active, c.qualification = :qualification WHERE c.fan = :fan
        queryToList.add("teamUserByFan"); //query = SELECT tu from TeamUser tu WHERE tu.fan = :fan
        queryToList.add("closedTeamsList"); //query = SELECT t FROM Team t  WHERE t.close = true
        queryToList.add("teamByName"); //query = SELECT t FROM Team t WHERE t.name = :name
        queryToList.add("teamsByStadium"); //query = SELECT t FROM Team t  WHERE :stadium IN (t.stadiums))
        queryToList.add("updateStadiumsToTeam"); //query = UPDATE Team t SET t.stadiums = :newStadiumsList WHERE t.name = :name AND t.close = false
        //queryToList.add("setStadiumActivity"); // query = UPDATE Stadium s SET s.active = :active WHERE s.name = :name
        //queryToList.add("SetTeamActivity"); // query = UPDATE Team t SET t.active = :active WHERE t.name = :name AND t.close = false
        queryToList.add("closeTeam"); //query = UPDATE Team t SET t.close = true, t.active = false WHERE t.name = :name AND t.close = false
        //queryToList.add("SetActiveTeamUser"); //query = UPDATE TeamUser tu SET tu.active = : active where tu =: teamUser
        queryToList.add("teamUserByTeam"); //query = SELECT tu from TeamUser tu WHERE tu.team = :team
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("stadiumByName")) {
            //query = SELECT s FROM Stadium s WHERE s.name = :name
            List<Stadium> result = new ArrayList<>();

            for(Stadium stadium : stadiums)
            {
                if(stadium.getName().equals(parameters.get("name")))
                    result.add(stadium);
            }

            return result;
        }

        else if(queryName.equals("stadiumsByTeam")) {
            //query = SELECT s FROM Stadium s WHERE :team IN (s.teams) AND s.team.close = false
            List<Stadium> result = new ArrayList<>();

            for(Stadium stadium : stadiums)
            {
                Team team = (Team)parameters.get("team");
                if(stadium.getTeams().contains(team) && !team.isClose())
                    result.add(stadium);
            }

            return result;
        }

        else if(queryName.equals("playersByTeam")) {
            // query = SELECT p FROM Player p WHERE p.team = :team AND p.team.close = false AND p.active = true
            List<Player> result = new ArrayList<>();

            for(Player player : players)
            {
                Team team = (Team)parameters.get("team");
                if(player.getTeam().equals(team) && !team.isClose() && player.isActive()) result.add(player);
            }

            return result;
        }

        else if(queryName.equals("coachesByTeam")) {
            // query = SELECT c FROM Coach c WHERE c.team = :team AND c.active = true AND c.team.close = false
            List<Coach> result = new ArrayList<>();

            for(Coach coach : coaches)
            {
                Team team = (Team)parameters.get("team");
                if(coach.getTeam().equals(team) && !team.isClose() && coach.isActive()) result.add(coach);
            }

            return result;
        }

        else if(queryName.equals("teamUserByFan")) {
            // query = SELECT tu from TeamUser tu WHERE tu.fan = :fan
            List<TeamUser> result = new ArrayList<>();

            for(TeamUser teamUser : teamUsers)
            {
                if(teamUser.getFan().equals(parameters.get("fan"))) result.add(teamUser);
            }


            return result;
        }

        else if (queryName.equals("teamsByStadium")) {
            // query = "SELECT t FROM Team t  WHERE :stadium IN (t.stadiums)")
            List<Team> result = new ArrayList<>();

            for(Team team : teams)
            {
                if(team.getStadiums().contains(parameters.get("stadium"))) result.add(team);
            }

            return result;
        }

        else if(queryName.equals("closedTeamsList")) {
            // query = SELECT t FROM Team t  WHERE t.close = true
            List<Team> result = new ArrayList<>();

            for(Team team : teams)
            {
                if(team.isClose()) result.add(team);
            }

            return result;
        }

        else if(queryName.equals("teamByName")) {
            // query = SELECT t FROM Team t WHERE t.name = :name
            List<Team> result = new ArrayList<>();

            for(Team team : teams)
            {
                if(team.getName().equals(parameters.get("name"))) result.add(team);
            }

            return result;
        }

        else if (queryName.equals("teamUserByTeam")) {
            // query = SELECT tu from TeamUser tu WHERE tu.team = :team
            List<TeamUser> result = new ArrayList<>();

            for(TeamUser teamUser : teamUsers)
            {
                if(teamUser.getTeam().equals(parameters.get("team"))) result.add(teamUser);
            }

            return result;

        }

        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("setStadiumDetails")) {
            // query = UPDATE Stadium s SET s.name = :newName, s.capacity = :newCapacity WHERE s.name = :name
            List<Stadium> result = query("stadiumByName", parameters);

            if (result.isEmpty()) return false;

            Stadium stadium = result.get(0);

            return stadium.setDetails((String)parameters.get("newName"), (int)parameters.get("newCapacity"), (List<Team>)parameters.get("teamsList"));
        }

        else if(queryName.equals("updatePlayerDetails")) {
            // query = UPDATE Player p SET p.name = :name, p.role = :role, p.team = :team, p.active = :active, p.birthDate = :birthDate WHERE p.fan = :fan
            List<Player> result = query("teamUserByFan", parameters);

            TeamUser teamUser = result.get(0);

            if (result.isEmpty() || !(teamUser instanceof Player))
                return false;

            Player player = result.get(0);

            return player.setDetails((String)parameters.get("name"), (String)parameters.get("role"), (Team)parameters.get("team"), (boolean)parameters.get("active"), (Date)parameters.get("birthDate"));
        }

        else if(queryName.equals("updateCoachDetails")) {
            // query = UPDATE Coach c SET c.name = :name, c.role = :role, c.team = :team, c.active = :active, c.qualification = :qualification WHERE c.fan = :fan
            List<Coach> result = query("teamUserByFan", parameters);

            if (result.isEmpty() || !(result.get(0) instanceof Coach)) return false;

            Coach coach = result.get(0);

            return coach.setDetails((String)parameters.get("name"), (String)parameters.get("role"), (Team)parameters.get("team"), (boolean)parameters.get("active"), (String)parameters.get("qualification"));
        }

        else if (queryName.equals("updateStadiumsToTeam")) {
            // query = UPDATE Team t SET t.stadiums = :newStadiumsList WHERE t.name = :name AND t.close = false
            List<Team> result = query("teamByName", parameters);

            if (result.isEmpty()) return false;

            Team team = result.get(0);

            return team.setStadiumsList((List<Stadium>)parameters.get("newStadiumsList"));

        }

        else if (queryName.equals("setStadiumActivity")) {
            // query = UPDATE Stadium s SET s.active = :active WHERE s.name = :name
            List<Stadium> result = query("stadiumByName", parameters);

            if (result.isEmpty()) return false;

            Stadium stadium = result.get(0);

            stadium.setActive((boolean)parameters.get("active"));

            return (stadium.isActive());

        }

        else if (queryName.equals("SetTeamActivity")) {
            // query = UPDATE Team t SET t.active = :active WHERE t.name = :name AND t.close = false
            List<Team> result = query("teamByName", parameters);

            if (result.isEmpty()) return false;

            Team team = result.get(0);

            if ((boolean)parameters.get("active") == false && nextMatches(team))
                return false;

            team.setActive((boolean)parameters.get("active"));

            return team.isActive();

        }

        else if (queryName.equals("closeTeam")) {
            // query = UPDATE Team t SET t.close = true, t.active = false WHERE t.name = :name AND t.close = false
            List<Team> result = query("teamByName", parameters);

            if (result.isEmpty()) return false;

            Team team = result.get(0);

            if (nextMatches(team)) return false;

            team.setClose(true);

            return team.isClose();
        }

        else if (queryName.equals("SetActiveTeamUser")) {
            // query = UPDATE TeamUser tu SET tu.active = :active where tu = :teamUser

            TeamUser teamUser = (TeamUser) parameters.get("teamUser");

            if (teamUser == null) return false;

            teamUser.setActive((boolean)parameters.get("active"));

            return teamUser.isActive();

        }

        return false;
    }

    @Override
    public boolean insert(Object toInsert)
    {
        if(toInsert instanceof Player)
        {
            Player player = (Player) toInsert;
            players.add(player);
            player.getTeam().getPlayers().add(player);
        }
        else if(toInsert instanceof Coach)
        {
            Coach coach = (Coach) toInsert;
            coaches.add((Coach) toInsert);
            coach.getTeam().getCoaches().add(coach);
        }
        else if (toInsert instanceof Stadium)
        {
            Stadium stadium = (Stadium)toInsert;
            stadiums.add(stadium);
            stadium.getTeams().get(0).getStadiums().add((Stadium)toInsert);
        }

        return true;
    }

    @Override
    public boolean delete(Object toDelete)
    {
        return true;
    }

    private boolean nextMatches(Team team) {

        List<Match> matches = team.getAwayMatches();
        for (Match m : matches) {
            if (m.getEndTime() == null) return true;
        }
        matches = team.getHomeMatches();
        for (Match m : matches) {
            if (m.getEndTime() == null) return true;
        }

        return false;
    }
}
