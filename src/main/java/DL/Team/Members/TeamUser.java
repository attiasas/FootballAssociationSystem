package DL.Team.Members;
import javax.persistence.*;

import DL.Team.Team;
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
        @NamedQuery(name = "teamUserByTeam", query = "SELECT tu from TeamUser tu WHERE tu.team = :team"),
        @NamedQuery(name = "allActiveTeamUser", query = "SELECT tu from TeamUser tu WHERE tu.active = :active"),
        @NamedQuery(name = "teamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan"),
        @NamedQuery(name = "deactivateTeamUser", query = "UPDATE TeamUser tu SET tu.active = false WHERE tu.fan = :fan"),
})


public class TeamUser
{
    @Id
    @Column
    @OneToOne
    protected Fan fan;

    @Column
    protected String name;

    @Column
    protected boolean active;

    @Column
    @OneToOne(cascade = CascadeType.ALL)
    protected Team team;

    public TeamUser(String name, boolean active, Fan fan, Team team) {

        if (!onlyLettersString(name) || fan == null || team == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.active = active;
        this.fan = fan;
        this.team = team;
    }

    public TeamUser() {}

    protected boolean onlyLettersString(String str) {

        return str != null && str.matches("([a-zA-Z]+(\\s[a-zA-Z]*)*)+");
    }

    public boolean isActive() {
        return active;
    }

    public Fan getFan() {
        return fan;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Team getTeam() {
        return team;
    }
}
