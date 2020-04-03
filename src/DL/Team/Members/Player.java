package DL.Team.Members;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;

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
        //TODO - after merging all parts together - create a team update for a player (by player's username - inherits from user)


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
    public Player(Date birthDate, String role, Team team, UserPage page) {
        this.birthDate = birthDate;
        this.role = role;
        this.team = team;
        super.page = page;
    }

    public Player() {}
}
