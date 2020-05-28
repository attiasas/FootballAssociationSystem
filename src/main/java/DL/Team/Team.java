package DL.Team;

import DL.Administration.Financial.TeamFinancialEntry;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;
import DL.Users.User;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.*;

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
public class Team implements Serializable
{
    @Id
    private String name;

    @Column
    private boolean active;

    @Column
    private boolean close;

    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private TeamPage page;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<Coach> coaches;

    @OneToMany(mappedBy = "team",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<Player> players;

    @OneToMany(mappedBy = "team",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<TeamManager> teamManagers;

    @OneToMany(mappedBy = "team",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<TeamOwner> teamOwners;

    @ManyToMany(mappedBy = "teams", cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<Stadium> stadiums;

    @OneToMany(mappedBy = "homeTeam", cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<Match> homeMatches;

    @OneToMany(mappedBy = "awayTeam", cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<Match> awayMatches;

    @OneToMany(mappedBy = "team",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<TeamFinancialEntry> teamFinancialEntries;

    @ManyToMany(mappedBy = "teamsParticipate", cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private List<LeagueSeason> leagueSeasons;

    //Constructor
    public Team(String name, boolean active, boolean close) {

        if (!isValidTeamName(name)) return;

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
        teamFinancialEntries = null;
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

    public boolean addLeagueSeason(LeagueSeason leagueSeason) {

        if (leagueSeason == null) return false;

        leagueSeasons.add(leagueSeason);
        return true;
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

    public boolean addStadium(Stadium stadium) {

        if (stadium == null) return false;

        stadiums.add(stadium);

        return true;
    }

    public boolean addPlayer(Player player) {

        if (player == null) return false;

        players.add(player);

        return true;
    }

    public boolean removePlayer(Player player) {

        if (player == null) return false;

        return players.remove(player);

    }

    public boolean addCoach(Coach coach) {

        checkCoachInput(coach);

        coaches.add(coach);

        return true;
    }

    public boolean removeCoach(Coach coach) {

        checkCoachInput(coach);

        return coaches.remove(coach);

    }

    private void checkCoachInput(Coach coach) {

        String err = "";
        if (coach == null) {
            err += "Coach: Coach doesn't exist. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

    }

    public boolean removeStadium(Stadium stadium) {

        if (stadium == null) return false;

        return stadiums.remove(stadium);

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

    public boolean addTeamManagers(TeamManager teamManager) {

        if (teamManager == null)
            return false;

        teamManagers.add(teamManager);

        return true;

    }

    public List<TeamOwner> getTeamOwners() {
        return teamOwners;
    }

    public Set getTeamMembers() {
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

    public boolean hasActiveObjectsConnected() {

        for (Match m : homeMatches) {
            if (m.getEndTime() == null) {
                return false;
            }
        }

        for (Match m : awayMatches) {
            if (m.getEndTime() == null) {
                return false;
            }
        }


        boolean ans = (!players.isEmpty() || !coaches.isEmpty() || !teamManagers.isEmpty() ||
                !teamOwners.isEmpty() || !stadiums.isEmpty());

        return ans;

    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Team))
        {
            return false;
        }

        Team otherTeam = (Team)other;

        return this.name.equals(otherTeam.name);
    }

}
