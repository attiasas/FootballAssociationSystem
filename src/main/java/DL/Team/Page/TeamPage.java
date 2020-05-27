package DL.Team.Page;

import DL.Team.Members.TeamUser;
import DL.Team.Team;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: Defines a Page object - a personal page of team, a fan can follow    X
 * ID:              X
 **/
@NamedQueries(value = {
        @NamedQuery(name = "TeamPage", query = "SELECT tp from TeamPage tp"),
        @NamedQuery(name = "TeamPageSetContent", query = "UPDATE TeamPage tp SET tp.content = :content WHERE tp.team = :team "),
        @NamedQuery(name = "TeamPageByTeam", query = "SELECT tp from TeamPage tp WHERE team = :team")
})
@Entity
@DiscriminatorValue(value = "TeamPage")
public class TeamPage extends Page implements Serializable
{
    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private Team team;

    public TeamPage(String content, Team team) {

        String err = "";
        if (team == null) {
            err += "Team: Team doesn't exist. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        this.team = team;
        if (content != null)
            super.content = content;
        else
            super.content = "";
    }

    public Team getTeam()
    {
        return team;
    }



    public TeamPage() {

    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof TeamPage))
        {
            return false;
        }

        TeamPage otherTeamPage = (TeamPage)other;
        return this.team.equals(otherTeamPage.team);
    }
}
