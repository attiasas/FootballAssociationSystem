package DL.Team.Page;

import DL.Team.Team;

import javax.persistence.*;

/**
 * Description: Defines a Page object - a personal page of coach/player/team fan can follow    X
 * ID:              X
 **/


@NamedQueries(value = {
        @NamedQuery(name = "TeamPage", query = "SELECT tp from TeamPage tp"),
        @NamedQuery(name = "TeamPageSetContent", query = "UPDATE TeamPage tp SET tp.content = :content WHERE tp.team = :team "),
})


@Entity
public class TeamPage extends Page{

    @Id
    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    public TeamPage(String content, Team team) {
        this.team = team;
        super.content = content;
    }

    public TeamPage() {

    }
}
