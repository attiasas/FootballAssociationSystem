package DL.Game.LeagueSeason;

import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.swing.table.TableColumn;
import java.io.Serializable;
import java.util.*;

/**
 * Description:     Represents the connection between League, Season, GamePolicy, ScorePolicy
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetAllLeagueSeasons", query = "SELECT ls From LeagueSeason ls WHERE ls.season =: season"),
        @NamedQuery(name = "GetLeagueSeason", query = "SELECT ls From LeagueSeason ls WHERE ls.season =: season AND ls.league =: league"),
        @NamedQuery(name = "UpdateScorePolicy", query = "UPDATE LeagueSeason ls SET ls.scorePolicy.winPoints = :winPoints, ls.scorePolicy.drawPoints = :drawPoints, ls.scorePolicy.losePoints = :losePoints WHERE ls.leagueSeasonID =:leagueSeasonID"),
        @NamedQuery(name = "UpdateLeagueSeasonRefereesList", query = "UPDATE LeagueSeason ls SET ls.referees = :newReferees WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonTeamList", query = "UPDATE LeagueSeason ls SET ls.teamsParticipate = :newTeamList WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonMatchList", query = "UPDATE LeagueSeason ls SET ls.matches = :matchList WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonRefereeList", query = "UPDATE LeagueSeason ls SET ls.referees = :refereesList WHERE  ls.league = : league AND ls.season =: season")

})
public class LeagueSeason implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int leagueSeasonID;

    @OneToOne
    private League league;

    @OneToOne
    private Season season;

    @OneToOne
    private GamePolicy gamePolicy;

    @OneToOne
    private ScorePolicy scorePolicy;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "leagueSeason")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Match> matches;

    @ManyToMany(mappedBy = "leagueSeasons")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Team> teamsParticipate;

    @ManyToMany(mappedBy = "leagueSeasons")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Referee> referees;

    @Column
    private Date startDate;

    /**
     * Ctor with parameters
     *
     * @param league
     * @param season
     * @param gamePolicy
     * @param scorePolicy
     * @param startDate
     */
    public LeagueSeason(League league, Season season, GamePolicy gamePolicy, ScorePolicy scorePolicy, Date startDate) {
        this.league = league;
        this.season = season;
        this.gamePolicy = gamePolicy;
        this.scorePolicy = scorePolicy;
        this.matches = new ArrayList<>();
        this.referees = new ArrayList<>();
        this.teamsParticipate = new ArrayList<>();
        if (startDate != null) {
            this.startDate = new Date(startDate.getTime());
        }
    }

    /**
     * Default Ctor
     */
    public LeagueSeason() {
        this(null, null, null, null, null);
    }

    /**
     * Adds team to the leagueSeason
     *
     * @param team to add
     */
    public boolean addTeam(Team team) {
        if (team != null && team.isActive() && !checkIfObjectExists(team, teamsParticipate)) {
            teamsParticipate.add(team);
            //team.addLeagueSeason(this);
            return true;
        }
        return false;
    }

    /**
     * Adds referee to the leagueSeason
     *
     * @param referee
     */
    public boolean addReferee(Referee referee) {
        if (referee != null && !checkIfObjectExists(referee, referees)) {
            this.referees.add(referee);
            referee.addLeagueSeason(this);
            return true;
        }
        return false;
    }

    /**
     * checks if the league is already running  - if not changes the score policy.
     *
     * @param scorePolicy
     * @return true if the score policy changed, else return false.
     */
    public boolean setScorePolicy(ScorePolicy scorePolicy) {
        Date firstMatchDate = this.firstMatchDate();
        if (scorePolicy != null && (firstMatchDate == null || new Date().before(firstMatchDate))) {
            this.scorePolicy = scorePolicy;
            return true;
        }
        return false;
    }

    /**
     * Starts the scheduling system in order to calculate the leagueSeason matches.
     *
     * @return true if the schedule succeeded - else returns false;
     */
    public boolean scheduleLeagueMatches() {
        if (gamePolicy != null)
            this.matches = this.gamePolicy.scheduleMatches(this);
        return matches.size() != 0;
    }

    /**
     * Sets referees for all of the matches
     */
    public boolean setRefereesInMatches() {
        int indexOfReferee = 0;
        if (matches != null && matches.size() > 0 && referees.size() > 2) {
            for (Match match : matches) {
                for (int i=0;i<3;i++) {
                    if (indexOfReferee >= referees.size())
                        indexOfReferee = 0;
                    match.setReferee(referees.get(indexOfReferee));
                    referees.get(indexOfReferee++).addMatch(match);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns sorted table of the leagueSeason according to the scorePolicy
     */
    public List<Map.Entry<Team, Integer[]>> getLeagueSeasonTable() {
        if (scorePolicy != null)
            return this.scorePolicy.calculateLeagueTable(this);
        return null;
    }

    /**************************************************************************
     ***************************Getters****************************************
     **************************************************************************/

    public int getLeagueSeasonID() {
        return leagueSeasonID;
    }

    /**
     * @return the league that represents the LeagueSeason
     */
    public League getLeague() {
        return league;
    }

    /**
     * @return the Date when the leagueSeason starts.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return the Season that represents the LeagueSeason
     */
    public Season getSeason() {
        return season;
    }

    /**
     * @return list of referees that participates in the LeagueSeason
     */
    public List<Referee> getReferees() {
        return referees;
    }

    /**
     * @return the list of the teams that participate the LeagueSeason
     */
    public List<Team> getTeamsParticipate() {
        return teamsParticipate;
    }

    /**
     * @return list of matches of this leagueSeason
     */
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * @return the gamePolicy of the leagueSeason
     */
    public GamePolicy getGamePolicy() {
        return gamePolicy;
    }

    /**
     * @return the scorePolicy of the leagueSeason
     */
    public ScorePolicy getScorePolicy() {
        return scorePolicy;
    }

    /**************************************************************************
     ***************************Setter****************************************
     **************************************************************************/
    public void setLeague(League league) {
        this.league = league;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setGamePolicy(GamePolicy gamePolicy) {
        this.gamePolicy = gamePolicy;
    }

    public void setTeamsParticipate(List<Team> teamsParticipate) {
        this.teamsParticipate = teamsParticipate;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    public void setLeagueSeasonID(int leagueSeasonID) {
        this.leagueSeasonID = leagueSeasonID;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * TODO:
     * Sets the leagueSeason matches
     *
     * @param matches list of matches after assignment
     */
    public void setGames(List<Match> matches) {
        if (matches != null) {
            this.matches.clear();
            this.matches.addAll(matches);
        }
    }

    /**************************************************************************
     *************************Private Methods**********************************
     **************************************************************************/

    /**
     * Checks if the league is already running
     *
     * @return Date of the first match or null if it doesn't have matches yet
     */
    private Date firstMatchDate() {
        Date firstGameDate = null;
        if (matches.size() > 0) {
            for (Match match : matches) {
                if (firstGameDate == null) {
                    firstGameDate = match.getStartTime();
                } else if (firstGameDate.after(match.getStartTime())) {
                    firstGameDate = match.getStartTime();
                }
            }
        }
        return firstGameDate;
    }

    /**
     * checks if the object already exists
     *
     * @param toCheck    object to find in the list
     * @param objectList list of objects
     * @return true if the object exists
     */
    private boolean checkIfObjectExists(Object toCheck, List objectList) {
        for (Object listObject : objectList) {
            if (listObject.equals(toCheck))
                return true;
        }
        return false;
    }


    /*************************************************************************
     *************************Override Methods*********************************
     **************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueSeason that = (LeagueSeason) o;
        return Objects.equals(league, that.league) &&
                Objects.equals(season, that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(league, season);
    }

    @Override
    public String toString() {
        return "LeagueSeason{" + league + ", " + season + '}';
    }


}
