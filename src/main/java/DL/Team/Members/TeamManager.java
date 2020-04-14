package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.util.List;

/**
 * Description:  Defines a TeamManager object - as a TeamUser object   X
 * ID:              X
 **/


@Entity
@NamedQueries(value = {
        @NamedQuery(name = "teamManager", query = "SELECT tm from TeamManager tm"),
        @NamedQuery(name = "teamManagerByTeam", query = "SELECT tm from TeamManager tm WHERE tm.team = :team"),
        @NamedQuery(name = "teamManagerTeamOwner", query = "SELECT tm from TeamManager tm WHERE tm.teamOwner = :teamOwner"),
})


public class TeamManager extends TeamUser
{
    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private TeamOwner teamOwner;

    //Constructor
    public TeamManager(String name, boolean active, Fan fan, Team team, TeamOwner teamOwner) {
        super(name, active, fan);
        this.team = team;
        this.teamOwner = teamOwner;
    }

    public TeamManager() {}
}