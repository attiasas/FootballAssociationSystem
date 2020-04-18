package BL.Client.Handlers;

import BL.Communication.CommunicationTeamAssetStub;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:  Test suite for TeamAssetUnit class  X
 * ID:   16           X
 **/
public class TeamAssetUnitTest {

    private List<Team> teams;
    private List<Stadium> stadiums;
    private List<Player> players;
    private List<Coach> coaches;
    private List<TeamUser> teamUsers;
    private List<Fan> fans;
    private List<String> queryToList;
    private TeamAssetUnit teamAssetUnit;

    @Before
    public void setUp() {

        teams = new ArrayList<>();
        stadiums = new ArrayList<>();
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        teamUsers = new ArrayList<>();
        fans = new ArrayList<>();
        teamAssetUnit = new TeamAssetUnit(new CommunicationTeamAssetStub(teams, stadiums, players, coaches, teamUsers));

        // Teams
        Team team1 = new Team("Real Madrid", true, false);
        Team team2 = new Team("Barcelona", true, false);
        Team team3 = new Team("Liverpool", true, false);
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);

        // Stadiums
        List<Team> teamsListBernabeu = new ArrayList<>();
        teamsListBernabeu.add(team1);
        teamsListBernabeu.add(team3);
        Stadium stadium1 = new Stadium("Santiago Bernabeu", 81000, team1);
        stadium1.setDetails("Santiago Bernabeu", 81000, teamsListBernabeu);

        List<Team> teamsListCampNou = new ArrayList<>();
        teamsListCampNou.add(team2);
        teamsListCampNou.add(team3);
        Stadium stadium2 = new Stadium("Camp Nou", 100000, team2);
        stadium2.setDetails("Camp Nou", 100000, teamsListCampNou);

        List<Team> teamsListAnfield = new ArrayList<>();
        teamsListAnfield.add(team1);
        teamsListAnfield.add(team2);
        teamsListAnfield.add(team3);
        Stadium stadium3 = new Stadium("Anfield", 60000, team3);
        stadium3.setDetails("Anfield", 60000, teamsListAnfield);

        stadiums.add(stadium1);
        stadiums.add(stadium2);
        stadiums.add(stadium3);

        team1.getStadiums().add(stadium1);
        team1.getStadiums().add(stadium3);

        team2.getStadiums().add(stadium2);
        team2.getStadiums().add(stadium3);

        team3.getStadiums().add(stadium1);
        team3.getStadiums().add(stadium2);
        team3.getStadiums().add(stadium3);

        // Fan
        Fan fan1 = new Fan("SergioR", "sergio@gmail.com", "123");
        Fan fan2 = new Fan("LeoM", "leo@gmail.com", "123");
        Fan fan3 = new Fan("ManeS", "mane@gmail.com", "123");
        Fan fan8 = new Fan("LukaM", "luka@gmail.com", "123");
        fans.add(fan1);
        fans.add(fan2);
        fans.add(fan3);
        fans.add(fan8);

        // Player
        Date date = new Date(12, 12, 1990);
        Player player1 = new Player("Sergio Ramos", true, fan1, date, "CB", team1);
        Player player2 = new Player("Leo Messi", true, fan2, date, "CF", team2);
        Player player3 = new Player("Sadio Mane", true, fan3, date, "LW", team3);
        Player player4 = new Player("Luka Modric", true, fan8, date, "LW", team1);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        teamUsers.add(player1);
        teamUsers.add(player2);
        teamUsers.add(player3);
        teamUsers.add(player4);

        team1.getPlayers().add(player1);
        team1.getPlayers().add(player4);

        team2.getPlayers().add(player2);

        team3.getPlayers().add(player3);

        // Coach
        Fan fan4 = new Fan("Zidane", "zidane@gmail.com", "123");
        Fan fan5 = new Fan("Setien", "setien@gmail.com", "123");
        Fan fan6 = new Fan("Klopp", "klopp@gmail.com", "123");
        Fan fan7 = new Fan("Barak", "barak@gmail.com", "123");
        fans.add(fan4);
        fans.add(fan5);
        fans.add(fan6);
        fans.add(fan7);

        Coach coach1 = new Coach("Zinedine Zidane", true, fan4, "UEFA Coach", "Primary Coach", team1);
        Coach coach2 = new Coach("Quique Setien", true, fan5, "UEFA Coach", "Primary Coach", team2);
        Coach coach3 = new Coach("Jurgen Klopp", true, fan6, "UEFA Coach", "Primary Coach", team3);
        Coach coach4 = new Coach("Barak Bachar", true, fan7, "UEFA Coach", "Secondary Coach", team1);
        coaches.add(coach1);
        coaches.add(coach2);
        coaches.add(coach3);
        coaches.add(coach4);
        teamUsers.add(coach1);
        teamUsers.add(coach2);
        teamUsers.add(coach3);
        teamUsers.add(coach4);


        team1.getCoaches().add(coach1);
        team1.getCoaches().add(coach4);

        team2.getCoaches().add(coach2);

        team3.getCoaches().add(coach3);

    }

    // ID: 16.1
    @Test
    public void testLoadTeamStadium() {

        List <Stadium> stadiums = teamAssetUnit.loadTeamStadium(teams.get(0));
        assertNotNull(stadiums);
        assertEquals(2, stadiums.size());
    }

    // ID: 16.2
    @Test
    public void testLoadTeamStadiumNullArgument() {

        List <Stadium> stadiums = teamAssetUnit.loadTeamStadium(null);
        assertNull(stadiums);
    }

    // ID: 16.3
    @Test
    public void testLoadTeamPlayers() {

        List <Player> players = teamAssetUnit.loadTeamPlayers(teams.get(0));
        assertNotNull(players);
        assertEquals(2, players.size());

    }

    // ID: 16.4
    @Test
    public void testLoadTeamPlayersNullArgument() {

        List <Player> players = teamAssetUnit.loadTeamPlayers(null);
        assertNull(players);

    }

    // ID: 16.5
    @Test
    public void testLoadTeamCoach() {
        List <Coach> coaches = teamAssetUnit.loadTeamCoach(teams.get(1));
        assertNotNull(coaches);
        assertEquals(1, coaches.size());
    }

    // ID: 16.6
    @Test
    public void testLoadTeamCoachNullArgument() {
        List <Coach> coaches = teamAssetUnit.loadTeamCoach(null);
        assertNull(coaches);
    }

    // ID: 16.7
    @Test
    public void testAddStadium() {

        boolean result = teamAssetUnit.addStadium("Old Trafford", 70000, teams.get(0));
        assertTrue(result);
        assertEquals(3, teams.get(0).getStadiums().size());

    }

    // ID: 16.8
    @Test
    public void testAddStadiumInvalidNameArgument() {

        boolean result = teamAssetUnit.addStadium("Terner$$$", 15000, teams.get(0));
        assertFalse(result);
        assertEquals(3, stadiums.size());
        assertEquals(2, teams.get(0).getStadiums().size());

    }

    // ID: 16.9
    @Test
    public void testAddStadiumInvalidTeamArgument() {

        boolean result = teamAssetUnit.addStadium("Terner", 15000, null);
        assertFalse(result);
        assertEquals(3, stadiums.size());

    }

    // ID: 16.10
    @Test
    public void updateStadium() {

        boolean result = teamAssetUnit.updateStadium("CampNou", 90000, teams, stadiums.get(1));
        assertTrue(result);
        assertEquals("CampNou", stadiums.get(1).getName());
        assertEquals(90000, stadiums.get(1).getCapacity());
        assertEquals(teams.size(), stadiums.get(1).getTeams().size());
    }

    // ID: 16.11
    @Test
    public void updateStadiumInvalidCapacity() {
        boolean result = teamAssetUnit.updateStadium("Camp Nou", 0, teams, stadiums.get(1));
        assertFalse(result);
        assertEquals(100000, stadiums.get(1).getCapacity());
    }

    // ID: 16.12
    @Test
    public void testRemoveStadium() {

        Team barcelona = teams.get(1);
        barcelona.getStadiums().remove(0); // remove camp nou from barcelona
        Team liverpool = teams.get(2);
        liverpool.getStadiums().remove(1); // remove camp nou from liverpool

        boolean result = teamAssetUnit.removeStadium("Camp Nou"); // if false - stadium isn't active
        assertFalse(result);
        assertFalse(stadiums.get(1).isActive());

    }

    // ID: 16.13
    @Test
    public void testImpossibleRemoveStadium() {

        // There are still teams related to this stadium
        boolean result = teamAssetUnit.removeStadium("Camp Nou");
        assertFalse(result);
    }

    // ID: 16.14
    @Test
    public void testUpdateTeamStadiums() {

        //set liverpool stadium (3 stadium) to real madrid (2 stadium)
        boolean result = teamAssetUnit.updateTeamStadiums(teams.get(0), teams.get(2).getStadiums());
        assertTrue(result);
        assertEquals(3, teams.get(0).getStadiums().size());

    }

    // ID: 16.15
    @Test
    public void testUpdateTeamStadiumsInvalidNumberOfStadiums() {

        List<Stadium> stadiums = new ArrayList<>();
        boolean result = teamAssetUnit.updateTeamStadiums(teams.get(0), stadiums);
        assertFalse(result);
        assertEquals(2, teams.get(0).getStadiums().size());

    }

    // ID: 16.16
    @Test
    public void testActivateTeam() {

        Team team = teams.get(0);
        team.setActive(false);
        assertFalse(team.isActive());
        boolean result = teamAssetUnit.setTeamActivity(team, true);
        assertTrue(result);
        assertTrue(team.isActive());
    }

    // ID: 16.17
    @Test
    public void testDeactivateTeam() {

        Team team = teams.get(0);
        boolean result = teamAssetUnit.setTeamActivity(team, false);
        assertFalse(result); // if false - team is no longer active
        assertFalse(team.isActive());
    }

    // ID: 16.18
    @Test
    public void testCloseTeam() {

        boolean result = teamAssetUnit.closeTeam(teams.get(0));
        assertTrue(result);
        assertTrue(teams.get(0).isClose());
    }

    // ID: 16.19
    @Test
    public void testCloseTeamAlreadyClosedTeam() {

        teams.get(0).setClose(true);
        boolean result = teamAssetUnit.closeTeam(teams.get(0));
        assertFalse(result);
    }

    // ID: 16.20
    @Test
    public void testCloseTeamInvalidArgument() {

        boolean result = teamAssetUnit.closeTeam(null);
        assertFalse(result);
    }

    // ID: 16.21
    @Test
    public void testAddPlayer() {

        Fan fan = new Fan("IscoA", "isco@gmail.com", "123");
        Date date = new Date(12, 12, 1990);
        boolean result = teamAssetUnit.addPlayer(teams.get(0), fan, "Isco Alarcon", "CM", date);
        assertTrue(result);
        assertEquals(3, teams.get(0).getPlayers().size());
    }

    // ID: 16.22
    @Test
    public void testAddPlayerInvalidFanArgument() {

        Date date = new Date(12, 12, 1990);
        boolean result = teamAssetUnit.addPlayer(teams.get(0), fans.get(0), "Isco Alarcon", "CM", date);
        assertFalse(result);
        assertEquals(2, teams.get(0).getPlayers().size());
    }

    // ID: 16.23
    @Test
    public void testAddPlayerInvalidRoleArgument() {

        Fan fan = new Fan("IscoA", "isco@gmail.com", "123");
        Date date = new Date(12, 12, 1990);
        boolean result = teamAssetUnit.addPlayer(teams.get(0), fan, "Isco Alarcon", "CM!", date);
        assertFalse(result);
        assertEquals(2, teams.get(0).getPlayers().size());
    }

    // ID: 16.24
    @Test
    public void testEditPlayer() {

        Date date = new Date(12, 12, 1990);
        boolean result = teamAssetUnit.editPlayer(teams.get(0), fans.get(0), "Sergio Ramos", "CM", date);
        assertTrue(result);
        assertEquals("CM", players.get(0).getRole());
    }

    // ID: 16.25
    @Test
    public void testEditPlayerInvalidFanArgument() {

        Date date = new Date(12, 12, 1990);
        boolean result = teamAssetUnit.editPlayer(teams.get(0), null, "Sergio Ramos", "CM", date);
        assertFalse(result);
    }

    // ID: 16.26
    @Test
    public void testEditPlayerInvalidTeamArgument() {

        Date date = new Date(12, 12, 1990);
        teams.get(1).setClose(true);
        boolean result = teamAssetUnit.editPlayer(teams.get(1), fans.get(0), "Sergio Ramos", "CM", date);
        assertFalse(result);
    }

    // ID: 16.27
    @Test
    public void testRemovePlayer() {

        boolean result = teamAssetUnit.removePlayer(fans.get(0));
        assertFalse(result); // if false - player is no longer active
        assertFalse(teamUsers.get(0).isActive());
    }

    // ID: 16.28
    @Test
    public void testRemovePlayerAlreadyInactive() {

        players.get(0).setActive(false);
        boolean result = teamAssetUnit.removePlayer(fans.get(0));
        assertFalse(result);
    }

    // ID: 16.29
    @Test
    public void testAddCoach() {

        Fan fan = new Fan("Dvir Simhon", "dvir@gmail.com", "123");
        boolean result = teamAssetUnit.addCoach(teams.get(0), fan, "Isco Alarcon", "UEFA", "Primary Coach");
        assertTrue(result);
        assertEquals(3, teams.get(0).getCoaches().size());
    }

    // ID: 16.30
    @Test
    public void testAddCoachInvalidFanArgument() {

        boolean result = teamAssetUnit.addCoach(teams.get(0), fans.get(0), "Isco Alarcon", "UEFA", "PrimaryCoach");
        assertFalse(result);
        assertEquals(2, teams.get(0).getCoaches().size());
    }

    // ID: 16.31
    @Test
    public void testAddCoachInvalidRoleArgument() {

        Fan fan = new Fan("Dvir Simhon", "dvir@gmail.com", "123");
        boolean result = teamAssetUnit.addCoach(teams.get(0), fan, "Isco Alarcon", "UEFA", "Pr!imary Coach");
        assertFalse(result);
        assertEquals(2, teams.get(0).getCoaches().size());
    }

    // ID: 16.32
    @Test
    public void testAddCoachWrongArgumentFanIsPlayer() {
        boolean result = teamAssetUnit.addCoach(teams.get(0), fans.get(0), "Isco Alarcon", "UEFA", "Pr!imary Coach");
        assertFalse(result);
        assertEquals(2, teams.get(0).getCoaches().size());
    }

    // ID: 16.33
    @Test
    public void testEditCoach() {

        boolean result = teamAssetUnit.editCoach(teams.get(0), fans.get(5), "Another Coach", "UEFA PRO", "Secondary Coach");
        assertTrue(result);
        assertEquals("Secondary Coach", coaches.get(1).getRole());
    }

    // ID: 16.34
    @Test
    public void testEditCoachInvalidFanArgument() {

        boolean result = teamAssetUnit.editCoach(teams.get(0), null, "Sergio Ramos", "PRIME", "UEFA");
        assertFalse(result);
    }

    // ID: 16.35
    @Test
    public void testEditCoachInvalidTeamArgument() {

        teams.get(1).setClose(true);
        boolean result = teamAssetUnit.editCoach(teams.get(1), fans.get(5), "Sergio Ramos", "ROLE", "QUAL");
        assertFalse(result);
    }

    // ID: 16.36
    @Test
    public void testRemoveCoach() {

        boolean result = teamAssetUnit.removeCoach(fans.get(5));
        assertFalse(result); // if false - coach is no longer active
        assertFalse(teamUsers.get(5).isActive());
    }

    // ID: 16.37
    @Test
    public void testRemoveCoachAlreadyInactive() {

        coaches.get(1).setActive(false);
        boolean result = teamAssetUnit.removePlayer(fans.get(5));
        assertFalse(result);
    }

    //ID: 16.38
    @Test
    public void testRemoveTeamWhileMatchesLeft() {
        Date date = new Date(1999, 12, 10);
        Date date1 = new Date(1999, 12, 13);
        League league = new League("La liga");
        Season season = new Season(1990);
        GamePolicy gamePolicy = new GamePolicy(12, 30);
        ScorePolicy scorePolicy = new ScorePolicy(60, 3, 3);
        LeagueSeason leagueSeason = new LeagueSeason(league, season, gamePolicy, scorePolicy, date);
        Match match = new Match(date1, teams.get(0), teams.get(1), leagueSeason, stadiums.get(0));
        teams.get(0).getHomeMatches().add(match);
        teams.get(1).getAwayMatches().add(match);
        boolean res = teamAssetUnit.setTeamActivity(teams.get(0), false);

        assertFalse(res);

    }

}