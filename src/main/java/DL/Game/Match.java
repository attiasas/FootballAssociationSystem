package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.MatchEvents.EventLog;
import DL.Team.Assets.Stadium;
import DL.Team.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Description:     This class represents a match between two teams
 **/

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int matchID;
    @Column
    private Date startTime;
    @Column
    private Date endTime;
    @Column
    private int homeScore;
    @Column
    private int awayScore;
    @ManyToOne
    private Team homeTeam;
    @ManyToOne
    private Team awayTeam;
    @ManyToOne
    private Referee firstLineManReferee;
    @ManyToOne
    private Referee secondLineManReferee; // 2 linesmen in a match
    @ManyToOne
    private Referee mainReferee;
    @ManyToOne
    private LeagueSeason leagueSeason;
    @OneToOne
    private EventLog myEventLog;
    @ManyToOne
    private Stadium stadium;


    public Match(Date startTime, Team homeTeam, Team awayTeam, LeagueSeason leagueSeason, Stadium stadium) {
        //deep copy
        if (startTime != null) {
            this.startTime = new Date(startTime.getTime());
        }
        if (homeTeam != null && awayTeam != null && !homeTeam.equals(awayTeam)) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }
        this.leagueSeason = leagueSeason;
        this.stadium = stadium;
        this.myEventLog = new EventLog();
        this.homeScore = this.awayScore = 0;

    }

    public Match() {
        this(null, null, null, null, null);
    }

    public void setMainReferee(Referee referee) {
        if (referee != null) {
            if (firstLineManReferee == null || secondLineManReferee == null || !referee.equals(firstLineManReferee) && !referee.equals(secondLineManReferee)) {
                this.mainReferee = referee;
            }
        }
    }

    public boolean setLinesManReferees(Referee firstReferee, Referee secondReferee) {
        if (firstReferee != null && secondReferee != null && !firstReferee.equals(secondReferee)) {
            if (!firstReferee.equals(mainReferee) && !secondReferee.equals(mainReferee)) {
                this.firstLineManReferee = firstReferee;
                this.secondLineManReferee = secondReferee;
                return true;
            }
        }
        return false;
    }

    public void setScore(int homeScore, int awayScore) {
        if (homeScore >= 0 && awayScore >= 0) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }
    }

    public void setStartTime(Date startTime) {
        if (startTime != null)
            this.startTime = new Date(startTime.getTime());
    }

    public void setEndTime(Date endTime) {
        if (endTime != null)
            this.endTime = new Date(endTime.getTime());
    }

    public void setStadium(Stadium stadium) {
        if (stadium != null)
            this.stadium = stadium;
    }

    /**********************************************
     ******************Getters*********************
     *********************************************/

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public EventLog getMyEventLog() {
        return myEventLog;
    }

    public Referee getFirstLineManReferee() {
        return firstLineManReferee;
    }

    public Referee getSecondLineManReferee() {
        return secondLineManReferee;
    }

    public Referee getMainReferee() {
        return mainReferee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(startTime, match.startTime) &&
                ((Objects.equals(homeTeam, match.homeTeam) &&
                        Objects.equals(awayTeam, match.awayTeam)) ||
                        (Objects.equals(homeTeam, match.awayTeam) &&
                                Objects.equals(awayTeam, match.homeTeam)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, homeTeam, awayTeam) + Objects.hash(startTime, awayTeam, homeTeam);
    }

}
