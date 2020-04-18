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
        @NamedQuery(name = "TeamUser", query = "SELECT tu from TeamUser tu"),
        @NamedQuery(name = "TeamUserByName", query = "SELECT tu from TeamUser tu WHERE tu.name = :name"),
        @NamedQuery(name = "AllActiveTeamUser", query = "SELECT tu from TeamUser tu WHERE tu.active = :active"),
        @NamedQuery(name = "TeamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan"),
        @NamedQuery(name = "ActiveTeamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan AND tu.active = true"),
        @NamedQuery(name = "AllTeamUsersByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan"),
        @NamedQuery(name = "SetActiveTeamUser", query = "UPDATE TeamUser tu SET tu.active = :active where tu = :teamUser"),
        @NamedQuery(name = "teamUser", query = "SELECT tu from TeamUser tu"),
        @NamedQuery(name = "teamUserByName", query = "SELECT tu from TeamUser tu WHERE tu.name = :name"),
        @NamedQuery(name = "teamUserByTeam", query = "SELECT tu from TeamUser tu WHERE tu.team = :team"),
        @NamedQuery(name = "allActiveTeamUser", query = "SELECT tu from TeamUser tu WHERE tu.active = :active"),
        @NamedQuery(name = "teamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan and active = :active"),
        @NamedQuery(name = "deactivateTeamUser", query = "UPDATE TeamUser tu SET tu.active = false WHERE tu.fan = :fan"),
        @NamedQuery(name = "ActivateTeamUserByFan", query = "UPDATE TeamUser tu SET tu.active = true WHERE tu.fan = :fan"),
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
    @OneToOne(cascade = CascadeType.ALL)
    protected Team team;

    @Column
    boolean active;

    public TeamUser(String name, boolean active, Fan fan, Team team) 
    {
        if (!onlyLettersString(name) || fan == null || team == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.active = active;
        this.fan = fan;
        this.team = team;
    }

    public TeamUser() {}

    public Fan getFan() { return fan; }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() 
    {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    protected boolean onlyLettersString(String str)
    {
        return str != null && str.matches("([a-zA-Z]+(\\s[a-zA-Z]*)*)+");
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof TeamUser))
        {
            return false;
        }
        return ((TeamUser)other).fan.equals(this.fan);
    }

}
