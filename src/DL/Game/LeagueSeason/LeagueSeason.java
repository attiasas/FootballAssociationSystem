package DL.Game.LeagueSeason;

import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents the connection between League, Season, GamePolicy, ScorePolicy
 * ID:              X
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "LeagueSeason", query = "SELECT ls From LeagueSeason ls WHERE ls.id = :id")
})
public class LeagueSeason
{
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private League league;
    @OneToOne
    private Season season;
    @OneToOne
    private GamePolicy gamePolicy;
    @OneToOne
    private ScorePolicy scorePolicy;
    @ManyToMany
    private List<Match> matches ;
    @ManyToMany
    private List<Team> teamsParticipate;
    @ManyToMany
    private List<Referee> referees;

    public LeagueSeason (League league, Season season, GamePolicy gamePolicy, ScorePolicy scorePolicy)
    {
        this.league = league;
        this.season = season;
        this.gamePolicy = gamePolicy;
        this.scorePolicy = scorePolicy;
        this.matches = new ArrayList<Match>();
    }

    public LeagueSeason()
    {
        this(null, null, null, null);
    }


    public void addTeam (Team team)
    {
        teamsParticipate.add(team);
    }

    public void addMatch (Match match)
    {
        matches.add(match);
    }

    public void addReferee(Referee referee)
    {
        this.referees.add(referee);
    }


}
