package DL.Team.Members;
import DL.Game.MatchEvents.Event;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:  Defines a player object - as a PageUser object and by a role and a team   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "playersByBirthDate", query = "SELECT p from Player p WHERE p.birthDate = :birthDate AND p.active = true"),
        @NamedQuery(name = "playersByRole", query = "SELECT p FROM Player p WHERE p.role = :role AND p.active = true "),
        @NamedQuery(name = "playersByTeam", query = "SELECT p FROM Player p WHERE p.team = :team AND p.team.close = false AND p.active = true"),
        @NamedQuery(name = "playerByName", query = "SELECT p FROM Player p WHERE p.name = :name "),
        @NamedQuery(name = "playerByFan", query = "SELECT p FROM Player p WHERE p.fan = :fan "),
        @NamedQuery(name = "updatePlayerDetails", query = "UPDATE Player p SET p.name = :name, p.role = :role, p.team = :team, p.active = :active, p.birthDate = :birthDate WHERE p.fan = :fan"),
        @NamedQuery(name = "updatePlayerEvents", query = "update Player p set p.playerEvents = :playerEvents where p =: player")
})

public class Player extends PageUser
{
    @Column
    private Date birthDate;

    @Column
    private String role;

    @Column
    @OneToMany
    private List<Event> playerEvents;

    //Constructor
    public Player(String name, boolean active, Fan fan, Date birthDate, String role, Team team)
    {
        super(name, active, fan, new UserPage(), team);
        if (birthDate == null || !onlyLettersString(role))
            throw new IllegalArgumentException();

        this.birthDate = birthDate;
        this.role = role;
        playerEvents = new ArrayList<>();
    }

    public Player() {}

    public List<Event> addEvent(Event event)
    {
        playerEvents.add(event);
        return playerEvents;
    }

    public boolean setDetails(String name, String role, Team team, boolean active, Date birthDate) {

        if (!onlyLettersString(name) || !onlyLettersString(role) || team == null || birthDate == null)
            return false;

        this.name = name;
        this.role = role;
        this.team = team;
        this.active = true;
        this.birthDate = birthDate;
        return true;
    }

    public String getRole() {
        return role;
    }

    public List<Event> getPlayerEvents() {
        return playerEvents;
    }
}
