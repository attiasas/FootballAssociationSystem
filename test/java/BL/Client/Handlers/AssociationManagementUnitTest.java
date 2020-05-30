package BL.Client.Handlers;

import BL.Communication.CommunicationAssociationManagementStub;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Before;
import org.junit.Test;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:  Test suite for AssociationManagementUnit class  X
 * ID:   17           X
 **/

public class AssociationManagementUnitTest {

    private List<Team> teams;
    private List<TeamOwner> teamOwners;
    private List<Referee> referees;
    private AssociationManagementUnit associationManagementUnit;

    @Before
    public void setUp() {
        teams = new ArrayList<>();
        teamOwners = new ArrayList<>();
        referees = new ArrayList<>();
        associationManagementUnit = new AssociationManagementUnit(new CommunicationAssociationManagementStub(teams, teamOwners, referees));
    }

    // ID: 17.1
    @Test
    public void testAddTeam() {

        Fan fan = new Fan("Dvir", "dvir@gmail.com", "123");
        boolean result = associationManagementUnit.addTeam("Hapoel Beer Sheva", "dvir", fan);
        assertTrue(result);
        assertEquals(1, teams.size());

    }

    // ID: 17.2
    @Test
    public void testAddTeamInvalidName() {

        Fan fan = new Fan("Dvir", "dvir@gmail.com", "123");
        boolean result = associationManagementUnit.addTeam("Hapoel Be'er Sheva", "dvir", fan);
        assertFalse(result);
        assertEquals(0, teams.size());
    }

    // ID: 17.3
    @Test
    public void testRemoveReferee() {
        Fan fan = new Fan("dvir", "dvir@gmail.com", "123");
        Referee referee = new Referee("dvir", "dvir", fan, true);
        referees.add(referee);
        associationManagementUnit.removeReferee(referee);
        assertFalse(referee.isActive());
    }

    // ID: 17.4
    @Test
    public void testRemoveRefereeMatchesLeft() {
        Date date = new Date(1999, 12, 10);
        Date date1 = new Date(1999, 12, 13);
        Team home = new Team("liverpool", true, false);
        Team away = new Team("real madrid", true, false);
        League league = new League("La liga");
        Season season = new Season(1990);
        GamePolicy gamePolicy = new GamePolicy(12, 30);
        ScorePolicy scorePolicy = new ScorePolicy(60, 3, 3);
        LeagueSeason leagueSeason = new LeagueSeason(league, season, gamePolicy, scorePolicy, date);
        Stadium stadium = new Stadium("anfield", 60000, home);
        Match match = new Match(date1, home, away, leagueSeason, stadium);
        Fan fan = new Fan("dvir", "dvir@gmail.com", "123");
        Referee referee = new Referee("dvir", "dvir", fan, true);
        referees.add(referee);
       // referee.addMainMatch(match);
        associationManagementUnit.removeReferee(referee);
        assertTrue(referee.isActive());
    }

    // ID: 17.5
    @Test
    public void testAddNewReferee() {

        Fan fan = new Fan("dvir", "dvir@gmail.com", "123");
        boolean status = associationManagementUnit.addNewReferee(fan, "dvir", "UEFA");
        assertTrue(status);
    }

}
