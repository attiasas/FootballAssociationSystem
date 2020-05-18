package DL.Team;

import DL.Administration.Financial.TeamFinancialEntry;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Page.TeamPage;
import DL.Users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Description:  Defines a Team object - consists of players, manager, coaches, etc.   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
    @NamedQuery(name = "Team", query = "SELECT t FROM Team t"),
    @NamedQuery(name = "teamByName", query = "SELECT t FROM Team t WHERE t.name = :name "),
    @NamedQuery(name = "activeTeam", query = "SELECT t FROM Team t WHERE t.active = true AND t.close = false"),
    @NamedQuery(name = "inActiveTeam", query = "SELECT t FROM Team t WHERE t.active = false "),
    @NamedQuery(name = "setActivity", query = "UPDATE Team t SET t.active = :active WHERE t.name = :name "),
    @NamedQuery(name = "closedTeam", query = "SELECT t FROM Team t  WHERE t.close = true"),
    @NamedQuery(name = "openTeam", query = "SELECT t FROM Team t  WHERE t.close = false"),
    @NamedQuery(name = "setStatus", query = "UPDATE Team t SET t.close = :close WHERE t.name = :name "),
    @NamedQuery(name = "updateTeamOwnersOfTeam", query = "update Team t set t.teamOwners = :teamOwners where t = :team"),
    @NamedQuery(name = "updateTeamManagersOfTeam", query = "update Team t set t.teamManagers = :teamManagers where t = :team"),
    @NamedQuery(name = "UpdateTeamLeagueSeasonList", query = "UPDATE Team t SET t.leagueSeasons = :newLeagueSeason WHERE t.name = :name "),
    @NamedQuery(name = "activateTeam", query = "UPDATE Team t SET t.active = true WHERE t.name = :name AND t.close = false "),
    @NamedQuery(name = "deactivateTeam", query = "UPDATE Team t SET t.active = false WHERE t.name = :name AND t.close = false"),
    @NamedQuery(name = "closedTeamsList", query = "SELECT t FROM Team t  WHERE t.close = true"),
    @NamedQuery(name = "openTeamsList", query = "SELECT t FROM Team t  WHERE t.close = false"),
    @NamedQuery(name = "teamsByStadium", query = "SELECT t FROM Team t  WHERE :stadium IN (t.stadiums)"),
    @NamedQuery(name = "closeTeam", query = "UPDATE Team t SET t.close = true, t.active = false WHERE t.name = :name AND t.close = false "),
    @NamedQuery(name = "updateStadiumsToTeam", query = "UPDATE Team t SET t.stadiums = :newStadiumsList WHERE t.name = :name AND t.close = false "),
})

public class Team {
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
    private List<Coach> coaches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Player> players;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<TeamManager> teamManagers;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<TeamOwner> teamOwners;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Stadium> stadiums;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Match> homeMatches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Match> awayMatches;

    @Column
    @OneToMany(cascade = CascadeType.MERGE)
    private List<TeamFinancialEntry> teamFinancialEntries;

    @Column
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<LeagueSeason> leagueSeasons;

    //Constructor
    public Team(String name, boolean active, boolean close) {

        if (!isValidTeamName(name))
            throw new IllegalArgumentException();

        this.name = name;
        this.active = active;
        this.close = close;
        this.page = new TeamPage();
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
        this("Default",false,false);
    }

    public boolean setPage (TeamPage page) {
        if(page == null)
        {
            return false;
        }
        this.page = page;
        return true;
    }

    public void addLeagueSeason(LeagueSeason leagueSeason) {
        if (leagueSeason != null)
            leagueSeasons.add(leagueSeason);
    }

    public String getName() {
        return name;
    }
    
    public List<LeagueSeason> getLeagueSeasons() {
        return leagueSeasons;
    }

    public List<Match> getAwayMatches() {
        return awayMatches;
    }

    public List<Match> getHomeMatches() {
        return homeMatches;
    }

    public void addStadium(Stadium stadium) {
        if (stadium != null)
            stadiums.add(stadium);
    }

    public void setHomeMatches(Match homeMatch) {
        if (homeMatches != null) {
            homeMatches.add(homeMatch);
        }
    }

    public void setAwayMatches(Match awayMatch) {
        if (awayMatch != null) {
            awayMatches.add(awayMatch);
        }
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Team))
        {
            return false;
        }

        Team otherTeam = (Team)other;

        return this.name.equals(otherTeam.name);
    }

    private boolean isValidTeamName(String name) {

        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }

    public boolean isClose() {
        return close;
    }

    public boolean setStadiumsList(List<Stadium> stadiums) {

        if (stadiums == null) return false;

        this.stadiums = stadiums;
        return true;
    }

    public boolean isActive(){
        return active;
    }

    public TeamPage getPage() {
        return page;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<TeamManager> getTeamManagers() {
        return teamManagers;
    }

    public List<TeamOwner> getTeamOwners() {
        return teamOwners;
    }

    public Set getTeamMembers()
    {
        Set<User> result = new HashSet<>();

        for(Coach coach : coaches) if(coach.isActive()) result.add(coach.getFan());
        for(Player player : players) if(player.isActive()) result.add(player.getFan());
        for(TeamManager manager : teamManagers) if(manager.isActive()) result.add(manager.getFan());
        for(TeamOwner owner : teamOwners) if(owner.isActive()) result.add(owner.getTeamUser().getFan());

        return result;
    }

    public List<TeamFinancialEntry> getTeamFinancialEntries() {
        return teamFinancialEntries;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public List<Stadium> getStadiums() {
        return stadiums;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }
}
