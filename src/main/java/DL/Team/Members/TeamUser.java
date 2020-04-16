package DL.Team.Members;
import javax.persistence.*;

import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;

import java.util.List;

/**
 * Description:  Defines a TeamUser object - a user related to a team object   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "TeamUser", query = "SELECT tu from TeamUser tu"),
        @NamedQuery(name = "TeamUserByName", query = "SELECT tu from TeamUser tu WHERE tu.name = :name"),
        @NamedQuery(name = "AllActiveTeamUser", query = "SELECT tu from TeamUser tu WHERE tu.active = :active"),
        @NamedQuery(name = "TeamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan"),
        @NamedQuery(name = "ActiveTeamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan AND tu.active = true"),
        @NamedQuery(name = "SetActiveTeamUser", query = "UPDATE TeamUser tu SET tu.active = : active where tu =: teamUser")
})
public class TeamUser
{
    @Id
    @Column
    String name;

    @Id
    @Column
    @OneToOne
    Fan fan;

    @Column
    boolean active;

    public TeamUser(String name, boolean active, Fan fan) {
        this.name = name;
        this.active = active;
        this.fan = fan;
    }

    public TeamUser() {}

    public Fan getFan() { return fan; }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }
}
