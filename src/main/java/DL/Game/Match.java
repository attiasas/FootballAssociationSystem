package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.MatchEvents.EventLog;
import DL.Team.Assets.Stadium;
import DL.Team.Team;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description:     This class represents a match between two teams
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "UpdateMatchEventLog", query = "update Match m set m.myEventLog = :eventLog where m = : match"),
        @NamedQuery(name = "UpdateMatchEndTime", query = "update Match m set m.endTime =: endTime where m =: match"),
        @NamedQuery(name = "nextMatchesListByTeam", query = "SELECT m FROM Match m WHERE m.endTime = null AND (m.homeTeam = :team OR m.awayTeam = :team)"),
//        @NamedQuery(name = "nextMatchesListByReferee", query = "SELECT m FROM Match m WHERE m.endTime = null AND (m.mainReferee = :referee OR m.firstLineManReferee = :referee OR m.secondLineManReferee = :referee)")
})
public class Match implements Serializable {

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

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Referee> referees; // maximum 3 referees

    @ManyToOne
    private LeagueSeason leagueSeason;

    @OneToOne(cascade = CascadeType.ALL)
    private EventLog myEventLog;

    @ManyToOne
    private Stadium stadium;

    /**
     * Ctor
     *
     * @param startTime
     * @param homeTeam
     * @param awayTeam
     * @param leagueSeason
     * @param stadium
     */
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
        this.myEventLog = new EventLog(this);
        this.homeScore = this.awayScore = 0;
        this.referees = new ArrayList<>();
    }

    /**
     * Default Ctor
     */
    public Match() {
        this(null, null, null, null, null);
    }

    /**
     * Sets mainReferee of the match. checks that not equals to the linesMan.
     *
     * @param referee not null
     */
    public void setReferee(Referee referee) {
        boolean exists = false;
        if (referee != null && referees.size() < 3) {
            for (Referee r : referees) {
                if (r.equals(referee))
                    exists = true;
            }
            if (!exists) referees.add(referee);
        }
    }

    /**
     * Sets the match score
     *
     * @param homeScore
     * @param awayScore
     */
    public void setScore(int homeScore, int awayScore) {
        if (homeScore >= 0 && awayScore >= 0) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }
    }

    /**
     * sets the start time of the match
     *
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        if (startTime != null)
            this.startTime = new Date(startTime.getTime());
    }

    /**
     * sets the end time of the match
     *
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        if (endTime != null)
            this.endTime = new Date(endTime.getTime());
        else this.endTime = null;
    }

    public void setMyEventLog(EventLog myEventLog) {
        this.myEventLog = myEventLog;
    }

    /**
     * sets the stadium time of the match
     *
     * @param stadium
     */
    public void setStadium(Stadium stadium) {
        if (stadium != null)
            this.stadium = stadium;
    }

    /**********************************************
     ******************Getters*********************
     *********************************************/

    public boolean isMatchEventPeriodOver() {
        //Convert -> Date.from(Instant.from(LocalDate.of(2019,1,1).atStartOfDay(ZoneId.systemDefault())))

        if (startTime == null) return false;
        Date current = new Date();
        Date limit = new Date(startTime.getTime() + (5 * 60 * 1000));
        return current.after(limit);
    }

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

    public List<Referee> getReferees() {
        return referees;
    }

    public LeagueSeason getLeagueSeason() {
        return leagueSeason;
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

    @Override
    public String toString() {
        return "" +
                matchID +
                ", " + homeTeam +
                " VS " + awayTeam +
                " | At: " + startTime;
    }
}
