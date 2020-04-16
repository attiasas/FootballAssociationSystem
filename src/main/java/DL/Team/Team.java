package DL.Team;

import DL.Administration.Financial.TeamFinancialEntry;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.*;
import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:  Defines a Team object - consists of players, manager, coaches, etc.   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Team", query = "SELECT t FROM Team t"),
        @NamedQuery(name = "teamByName", query = "SELECT t FROM Team t WHERE t.name = :name "),
        @NamedQuery(name = "activeTeam", query = "SELECT t FROM Team t WHERE t.active = true "),
        @NamedQuery(name = "inActiveTeam", query = "SELECT t FROM Team t WHERE t.active = false "),
        @NamedQuery(name = "setActivity", query = "UPDATE Team t SET t.active = :active WHERE t.name = :name "),
        @NamedQuery(name = "closedTeam", query = "SELECT t FROM Team t  WHERE t.close = true"),
        @NamedQuery(name = "openTeam", query = "SELECT t FROM Team t  WHERE t.close = false"),
        @NamedQuery(name = "setStatus", query = "UPDATE Team t SET t.close = :close WHERE t.name = :name "),
        @NamedQuery(name =  "updateTeamOwnersOfTeam", query = "update Team t set t.teamOwners = :teamOwners where t = :team"),
        @NamedQuery(name =  "updateTeamManagersOfTeam", query = "update Team t set t.teamManagers = :teamManagers where t = :team")
})

public class Team
{
    @Id
    @Column
    private String name;

    @Column
    private boolean active;

    @Column
    private boolean close;

    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private TeamPage page;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<Coach> coaches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<Player> players;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<TeamManager> teamManagers;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<TeamOwner> teamOwners;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<Stadium> stadiums;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<Match> homeMatches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<Match> awayMatches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    public List<TeamFinancialEntry> teamFinancialEntries;

    @Column
    @ManyToMany(cascade = CascadeType.MERGE)
    public List<LeagueSeason> leagueSeasons;

    //Constructor
    public Team(String name, boolean active, boolean close, TeamPage page) {
        this.name = name;
        this.active = active;
        this.close = close;
        this.page = page;
        coaches = new ArrayList<>();
        players = new ArrayList<>();
        teamManagers = new ArrayList<>();
        teamOwners = new ArrayList<>();
        stadiums = new ArrayList<>();
        homeMatches = new ArrayList<>();
        awayMatches = new ArrayList<>();
        teamFinancialEntries = new ArrayList<>();
        leagueSeasons = new ArrayList<>();
    }

    public Team() {

    }
}
