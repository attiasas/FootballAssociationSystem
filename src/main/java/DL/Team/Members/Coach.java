package DL.Team.Members;

import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;

import javax.persistence.*;
/**
 * Description:  Defines coach object by qualification, role and team it coaches   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "coach", query = "SELECT c from Coach c"),
        @NamedQuery(name = "coachesByQualification", query = "SELECT c from Coach c WHERE c.qualification = :qualification"),
        @NamedQuery(name = "coachesByRole", query = "SELECT c FROM Coach c WHERE c.role = :role "),
        @NamedQuery(name = "coachesByTeam", query = "SELECT c FROM Coach c WHERE c.team = :team "),
})

public class Coach extends PageUser
{
    @Column
    private String qualification;

    @Column
    private String role;

    @Column
    @OneToOne (cascade = CascadeType.MERGE)
    private Team team;

    //Constructor
    public Coach(String name, boolean active, Fan fan, String qualification, String role, Team team) {
        super(name, active, fan, new UserPage());
        this.qualification = qualification;
        this.role = role;
        this.team = team;
    }

    public Coach() {}

}