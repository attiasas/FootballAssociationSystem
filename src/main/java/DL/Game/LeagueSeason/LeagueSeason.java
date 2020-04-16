package DL.Game.LeagueSeason;

import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;

import javax.persistence.*;
import java.util.*;

/**
 * Description:     Represents the connection between League, Season, GamePolicy, ScorePolicy
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetAllLeagueSeasons", query = "SELECT ls From LeagueSeason ls WHERE ls.seaon =: season"),
        @NamedQuery(name = "UpdateScorePolicy", query = "UPDATE LeagueSeason ls SET ls.scorePolicy = :newScorePolicy WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonRefereesList", query = "UPDATE LeagueSeason ls SET ls.referees = :newReferees WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonTeamList", query = "UPDATE LeagueSeason ls SET ls.teamsParticipate = :newTeamList WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonMatchList", query = "UPDATE LeagueSeason ls SET ls.matches = :matchList WHERE  ls.league = : league AND ls.season =: season"),
        @NamedQuery(name = "UpdateLeagueSeasonRefereeList", query = "UPDATE LeagueSeason ls SET ls.referees = :refereesList WHERE  ls.league = : league AND ls.season =: season")

})
@IdClass(LeagueSeason.EntryPK.class)
public class LeagueSeason {

    /**
     * For Composite Primary Key
     */
    public class EntryPK {
        public League league;
        public Season season;
    }


    @OneToOne(cascade = CascadeType.ALL)
    private League league;
    @OneToOne(cascade = CascadeType.ALL)
    private Season season;
    @OneToOne(cascade = CascadeType.ALL)
    private GamePolicy gamePolicy;
    @OneToOne(cascade = CascadeType.ALL)
    private ScorePolicy scorePolicy;
    @OneToMany(targetEntity = Match.class)
    private List<Match> matches;
    @ManyToMany
    private List<Team> teamsParticipate;
    @ManyToMany
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
            startDate = new Date(startDate.getTime());
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
        if (team != null && !checkIfObjectExists(team, teamsParticipate)) {
            teamsParticipate.add(team);
            team.addLeagueSeason(this);
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
     * TODO:
     * Sets the leagueSeason matches
     *
     * @param matches list of matches after assignment
     */
    public void setGames(List<Match> matches) {
        if (matches != null) {
            this.matches.addAll(matches);
        }
    }

    /**
     * Sets referees for all of the matches
     */
    public void setRefereesInMatches() {
        int indexOfReferee = 0;
        if (matches != null && matches.size() > 0 && referees.size() > 2) {
            for (Match match : matches) {

                if (indexOfReferee >= referees.size())
                    indexOfReferee = 0;
                match.setMainReferee(referees.get(indexOfReferee));
                referees.get(indexOfReferee++).addMainMatch(match);

                if (indexOfReferee >= referees.size()) {
                    indexOfReferee = 0;
                    match.setLinesManReferees(referees.get(indexOfReferee++), referees.get(indexOfReferee));
                    referees.get(indexOfReferee).addLinesManMatch(match);
                    referees.get(indexOfReferee - 1).addLinesManMatch(match);
                } else if (indexOfReferee + 1 >= referees.size()) {
                    match.setLinesManReferees(referees.get(indexOfReferee), referees.get(0));
                    referees.get(indexOfReferee).addLinesManMatch(match);
                    referees.get(0).addLinesManMatch(match);
                    indexOfReferee = 1;
                } else {
                    match.setLinesManReferees(referees.get(indexOfReferee++), referees.get(indexOfReferee++));
                    referees.get(indexOfReferee - 1).addLinesManMatch(match);
                    referees.get(indexOfReferee - 2).addLinesManMatch(match);

                }
            }
        }
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
        return "LeagueSeason{" +
                "league=" + league +
                ", season=" + season +
                '}';
    }


}
