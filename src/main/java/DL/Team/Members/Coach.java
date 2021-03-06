package DL.Team.Members;

import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:  Defines coach object by qualification, role and team it coaches   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "coach", query = "SELECT c from Coach c"),
        @NamedQuery(name = "coachesByQualification", query = "SELECT c from Coach c WHERE c.qualification = :qualification AND c.active = true"),
        @NamedQuery(name = "coachesByRole", query = "SELECT c FROM Coach c WHERE c.role = :role AND c.active = true"),
        @NamedQuery(name = "coachesByTeam", query = "SELECT c FROM Coach c WHERE c.team = :team AND c.active = true AND c.team.close = false"),
        @NamedQuery(name = "updateCoachDetails", query = "UPDATE Coach c SET c.name = :name, c.role = :role, c.team = :team, c.active = :active, c.qualification = :qualification WHERE c.fan = :fan"),
})
@DiscriminatorValue(value = "Coach")
public class Coach extends PageUser implements Serializable
{
    @Column
    private String qualification;

    @Column
    private String role;


    //Constructor
    public Coach(String name, boolean active, Fan fan, String qualification, String role, Team team) {

        super(name, active, fan, new UserPage(), team);

        if (team == null || team.isClose() || fan == null || !onlyLettersString(name) || !onlyLettersString(qualification) || !onlyLettersString(role))
            return;

        this.qualification = qualification;
        this.role = role;
        this.team = team;
    }

    public Coach() {}

    public Team getTeam() {
        return team;
    }

    public boolean setDetails(String name, String role, Team team, boolean active, String qualification) {

        if (team == null || team.isClose() || !onlyLettersString(name) || !onlyLettersString(qualification) || !onlyLettersString(role))
            return false;

        this.name = name;
        this.role = role;
        this.team = team;
        this.active = active;
        this.qualification = qualification;
        return true;

    }

    public String getRole() {
        return role;
    }
}
