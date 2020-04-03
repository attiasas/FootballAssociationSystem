package DL.Team.Members;

import DL.Team.Team;
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
        //TODO - after merging all parts together - create a team update for a TeamManager (by TeamManager's username - inherits from user)
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
    public TeamManager(String userName, String email, String hashedPassword, List<UserPermission.Permission> permissionList, Team team) {
        super(userName, email, hashedPassword, permissionList);
        this.team = team;
    }

    public TeamManager(String userName, String email, String hashedPassword, Team team) {
        super(userName, email, hashedPassword);
        this.team = team;
    }

    public TeamManager() {}
}
