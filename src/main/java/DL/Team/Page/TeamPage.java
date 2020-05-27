package DL.Team.Page;

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
    @OneToOne(cascade = {CascadeType.ALL})
    private Team team;

    public TeamPage(String content, Team team) {

        if (team == null || content == null)
            throw new IllegalArgumentException();

        this.team = team;
        super.content = content;
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
