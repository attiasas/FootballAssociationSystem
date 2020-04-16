package DL.Team.Members;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;

import javax.persistence.*;
import java.util.Date;

/**
 * Description:  Defines a player object - as a PageUser object and by a role and a team   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "playersByBirthDate", query = "SELECT p from Player p WHERE p.birthDate = :birthDate"),
        @NamedQuery(name = "playersByRole", query = "SELECT p FROM Player p WHERE p.role = :role "),
        @NamedQuery(name = "playersByTeam", query = "SELECT p FROM Player p WHERE p.team = :team "),
        @NamedQuery(name = "playerByName", query = "SELECT p FROM Player p WHERE p.name = :name "),
        @NamedQuery(name = "playerByFan", query = "SELECT p FROM Player p WHERE p.fan = :fan "),
        @NamedQuery(name = "playerByFan", query = "UPDATE Player p SET p.name = :name, p.role = :role, p.team = :team, p.active = :active, p.birthDate = :birthDate WHERE p.fan = :fan "),
})

public class Player extends PageUser
{
    @Column
    private Date birthDate;

    @Column
    private String role;

    @Column
    @OneToOne (cascade = CascadeType.MERGE)
    private Team team;


    //Constructor
    public Player(String name, boolean active, Fan fan, Date birthDate, String role, Team team) {
        super(name, active, fan, new UserPage());
        this.birthDate = birthDate;
        this.role = role;
        this.team = team;
    }


    public Player() {}
}
