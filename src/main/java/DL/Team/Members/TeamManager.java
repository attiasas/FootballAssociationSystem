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
        @NamedQuery(name = "activeTeamManager", query = "SELECT tm from TeamManager tm WHERE tm.active = true"),
        @NamedQuery(name = "teamManagerByTeam", query = "SELECT tm from TeamManager tm WHERE tm.team = :team AND tm.active = true AND tm.team.close = false"),
        @NamedQuery(name = "teamManagerTeamOwner", query = "SELECT tm from TeamManager tm WHERE tm.teamOwner = :teamOwner AND tm.active = true"),
})


public class TeamManager extends TeamUser
{

    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private TeamOwner teamOwner;

    //Constructor
    public TeamManager(String name, boolean active, Fan fan, Team team, TeamOwner teamOwner) {

        super(name, active, fan, team);

        if (fan == null || teamOwner == null)
            throw new IllegalArgumentException();

        this.team = team;
        this.teamOwner = teamOwner;
    }

    public TeamManager() {}
}
