package DL.Team.Members;

import DL.Game.MatchEvents.Event;
import DL.Team.Team;
import DL.Users.Fan;

import javax.persistence.*;
import java.io.Serializable;

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
        @NamedQuery(name = "teamUserByFan", query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan and tu.active = true"),
        @NamedQuery(name = "deactivateTeamUser", query = "UPDATE TeamUser tu SET tu.active = false WHERE tu.fan = :fan"),
        @NamedQuery(name = "ActivateTeamUserByFan", query = "UPDATE TeamUser tu SET tu.active = true WHERE tu.fan = :fan"),
})

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public class TeamUser implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    /**
     * For Composite Primary Key
     */

    @Column
    String name;

    @OneToOne(cascade = CascadeType.MERGE)
    Fan fan;

    @ManyToOne
    protected Team team;

    @Column
    boolean active;

    public TeamUser(String name, boolean active, Fan fan, Team team) 
    {
        if (!onlyLettersString(name) || fan == null || team == null) return;

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

    public void setTeam(Team team) { this.team = team; }

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
