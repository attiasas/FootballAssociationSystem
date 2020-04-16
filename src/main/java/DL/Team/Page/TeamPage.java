package DL.Team.Page;

import DL.Team.Team;

import javax.persistence.*;

/**
 * Description: Defines a Page object - a personal page of team, a fan can follow    X
 * ID:              X
 **/


@NamedQueries(value = {
        @NamedQuery(name = "TeamPage", query = "SELECT tp from TeamPage tp WHERE tp.team.close = false"),
        @NamedQuery(name = "TeamPageSetContent", query = "UPDATE TeamPage tp SET tp.content = :content WHERE tp.team = :team AND tp.team.close = false"),
})


@Entity
public class TeamPage extends Page{

    @Id
    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    public TeamPage(String content, Team team) {

        if (team == null || content == null)
            throw new IllegalArgumentException();

        this.team = team;
        super.content = content;
    }

    public TeamPage() {

    }
}
