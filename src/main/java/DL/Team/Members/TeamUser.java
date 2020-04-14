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
        @NamedQuery(name = "teamUser", query = "SELECT tu from TeamUser tu"),
        @NamedQuery(name = "teamUserByName", query = "SELECT tu from TeamUser tu WHERE tu.name = :name"),
        @NamedQuery(name = "allActiveTeamUser", query = "SELECT tu from TeamUser tu WHERE tu.active = :active"),
        @NamedQuery(name = "teamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan"),
})


public class TeamUser
{
    @Id
    @Column
    String name;

    @OneToOne
    @Column
    Fan fan;

    @Column
    boolean active;

    public TeamUser(String name, boolean active, Fan fan) {
        this.name = name;
        this.active = active;
        this.fan = fan;
    }

    public TeamUser() {}

}