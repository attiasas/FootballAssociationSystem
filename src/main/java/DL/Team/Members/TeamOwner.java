package DL.Team.Members;

import DL.Administration.Financial.FinancialEntry;
import DL.Administration.Financial.FinancialUser;
import DL.Team.Team;
import DL.Users.Fan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 * Description:  Defines a TeamOwner object - Owner of a specific team  X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "TeamOwnerByUser", query = "SELECT to FROM TeamOwner to WHERE to.active = true and to.teamUser.fan = :user"),
        @NamedQuery(name = "setActiveTeamOwner", query = "UPDATE TeamOwner to SET to.active = : active where to =: teamOwner"),
        @NamedQuery(name = "TeamOwner", query = "SELECT to FROM TeamOwner to WHERE to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByTeam", query = "SELECT to FROM TeamOwner to WHERE to.team = :team AND to.active = true AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByTeamUser", query = "SELECT to FROM TeamOwner to WHERE to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByNominee", query = "SELECT to FROM TeamOwner to WHERE to.ownerNominees = :nominee AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerAddOwnerNominee", query = "UPDATE TeamOwner to SET to.ownerNominees = :ownerNominee WHERE  to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerAddManageNominee", query = "UPDATE TeamOwner to SET to.ownerNominees = :manageNominee WHERE  to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "setTeamToTeamOwner", query = "UPDATE TeamOwner to SET to.team = :team WHERE to.teamUser = :teamUser"),
        @NamedQuery(name = "deactivateTeamOwner", query = "UPDATE TeamOwner to SET to.active = :active WHERE to.teamUser = :teamUser AND to.team.close = false"),
})
public class TeamOwner implements FinancialUser, Serializable
{
    @Id
    @GeneratedValue
    int id;

    @OneToOne(cascade = {CascadeType.ALL})
    private TeamUser teamUser;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Team team;

    @OneToMany
    private List<TeamOwner> ownerNominees;

    @OneToMany(mappedBy = "teamOwner" ,cascade = {CascadeType.MERGE})
    private List<TeamManager> manageNominees;

    @Column
    boolean active;

    public TeamOwner(Team team, TeamUser user)
    {
        if (team == null || user == null) return;

        this.teamUser = user;
        this.team = team;
        this.active = true;
        this.ownerNominees = new ArrayList<>();
        this.manageNominees = new ArrayList<>();
    }

    public TeamOwner() {}

    @Override
    public List<FinancialEntry> getFinancialEntries() {
        return null;
    }

    public List<TeamOwner> getOwnerNominees() {
        return ownerNominees;
    }

    public List<TeamManager> getManageNominees() {
        return manageNominees;
    }

    public Team getTeam() {
        return team;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public boolean isActive() {
        return active;
    }

    public TeamOwner addTeamOwnerNominee(TeamUser nominee)
    {
        String err = "";
        if(nominee == null) {
            err += "Team Owner Nominee: Team owner nominee doesn't exist. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamOwner owner = new TeamOwner(team,nominee);
        ownerNominees.add(owner);
        team.getTeamOwners().add(owner);
        return owner;
    }

    public TeamManager addTeamManagerNominee(Fan nominee, String name)
    {
        //if(nominee == null || name == null || name.isEmpty()) return null; already checked in team manager constructor

        TeamManager teamManager = new TeamManager(name,true,nominee,team,this);
        manageNominees.add(teamManager);
        team.addTeamManagers(teamManager);
        return teamManager;
    }

    public boolean isMyOwnerNominee(TeamOwner nominee)
    {
        if(nominee == null) return false;

        boolean found = false;

        for(int i = 0; i < ownerNominees.size() && !found; i++)
        {
            found = ownerNominees.get(i).equals(nominee);
        }

        return found;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTeam(Team team) { this.team = team; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamOwner)) return false;
        TeamOwner teamOwner = (TeamOwner) o;
        return teamUser.equals(teamOwner.teamUser);
    }

}
