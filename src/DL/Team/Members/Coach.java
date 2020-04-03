package DL.Team.Members;

import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;

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
        //TODO - after merging all parts together - create a team update for a coach (by coach'es username - inherits from user)
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
    public Coach(String qualification, String role, Team team, UserPage page) {
        this.qualification = qualification;
        this.role = role;
        this.team = team;
        super.page = page;
    }

    public Coach() {}

}
