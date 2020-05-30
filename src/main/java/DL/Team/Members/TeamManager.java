package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.io.Serializable;
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
@DiscriminatorValue(value = "TeamManager")
public class TeamManager extends TeamUser implements Serializable
{
    @ManyToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private TeamOwner teamOwner;

    //Constructor
    public TeamManager(String name, boolean active, Fan fan, Team team, TeamOwner teamOwner) {

        super(name, active, fan, team);

        String err = "";
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }
        if (teamOwner == null) {
            err += "Team Owner: Team owner doesn't exist. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        this.team = team;
        this.teamOwner = teamOwner;
    }

    public TeamManager() {}

    public TeamOwner getTeamOwner() {
        return teamOwner;
    }
}
