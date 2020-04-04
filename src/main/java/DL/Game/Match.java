package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.MatchEvents.EventLog;
import DL.Team.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Description:     X
 * ID:              X
 **/

@Entity
public class Match
{

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date startTime;
    @Column
    private Date endTime;
    @ManyToOne
    private Team home;
    @ManyToOne
    private Team away;
    @ManyToMany
    private Referee [] linesManReferees; // 2 linesmen in a match
    @ManyToOne
    private Referee mainReferee;
    @ManyToOne
    private LeagueSeason leagueSeason;
    @OneToOne
    private EventLog myEventLog;



    public Match (Date startTime, Date endTime, Team home, Team away, LeagueSeason leagueSeason, EventLog myEventLog)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.home = home;
        this.away = away;
        this.leagueSeason = leagueSeason;
        this.myEventLog = myEventLog;
    }

    public Match()
    {
        this(null, null, null, null, null, null);
    }

    public void setMainReferee (Referee referee)
    {
        this.mainReferee = referee;
    }

    public void setLinesManReferees(Referee firstReferee, Referee secondReferee)
    {
        this.linesManReferees[0] = firstReferee;
        this.linesManReferees[1] = secondReferee;
    }

}
