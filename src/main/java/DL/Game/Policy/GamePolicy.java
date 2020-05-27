package DL.Game.Policy;


import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Description:     This class represents a Game Policy
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetGamePolicies", query = "SELECT gp From GamePolicy gp"),
        @NamedQuery(name = "getGamePolicy", query = "SELECT gp FROM GamePolicy gp WHERE gp.numberOfRounds =: numberOfRounds AND gp.gamesPerDay =: gamesPerDay "),
})
@IdClass(GamePolicy.EntryPK.class)
public class GamePolicy implements Serializable{

    /**
     * For Composite Primary Key
     */
    public static class EntryPK implements Serializable {
        public int numberOfRounds;
        public int gamesPerDay;
    }

    @Id
    @Column
    private int numberOfRounds;
    @Id
    @Column
    private int gamesPerDay;

    /**
     * Ctor with parameters - minimum 1 round
     *
     * @param numberOfRounds
     */
    public GamePolicy(int numberOfRounds, int gamesPerDay) {
        if (numberOfRounds > 0) {
            this.numberOfRounds = numberOfRounds;
        } else {
            this.numberOfRounds = 1;
        }
        // no more than 7 games at day
        if (gamesPerDay > 0 && gamesPerDay <= 7) {
            this.gamesPerDay = gamesPerDay;
        } else {
            this.gamesPerDay = 1;
        }
    }

    /**
     * Default Ctor
     */
    public GamePolicy() {
        this(1, 1);
    }

    //setters
    /**
     * Gets number of rounds
     *
     * @return number of rounds
     */
    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    /**
     * @return number of games per day
     */
    public int getGamesPerDay() {
        return gamesPerDay;
    }

    //setters
    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public void setGamesPerDay(int gamesPerDay) {
        this.gamesPerDay = gamesPerDay;
    }

    /**
     * scheduling matches according to the number of rounds and number of teams on the given league.
     * uses round robin algorithm in order to schedule the matches.
     * The rules are:
     * 1) only 7 games possible at one day
     * 2) while the roundsNumber is greater than 1 the gamePolicy will be like the home/away technique.
     * 3) only one match can play at the same time - helps with the stadium scheduling.
     *
     * @param leagueSeason
     * @return list of matches
     */
    public List<Match> scheduleMatches(LeagueSeason leagueSeason) {
        if (leagueSeason != null) {
            //calculates first round and the rest of the rounds will be based on it.
            List<Match> matchesList = scheduleFirstRound(leagueSeason);
            Match matchToAdd;

            //add matches according to number of rounds needed - runs over the matchList and changes the HomeAway setting for each match.
            int totalMatchesLastToAdd = (numberOfRounds - 1) * matchesList.size();
            for (int i = 0; i < totalMatchesLastToAdd; i++) {
                matchToAdd = new Match(null, matchesList.get(i).getAwayTeam(), matchesList.get(i).getHomeTeam(), leagueSeason, null);
                addMatchToTeams(matchToAdd, matchesList.get(i).getAwayTeam(), matchesList.get(i).getHomeTeam());
                matchesList.add(matchToAdd);
            }

            setDates(matchesList, leagueSeason.getStartDate());
            setStadiums(matchesList);

            return matchesList;
        }
        return null;
    }

    /**
     * Calculates matches only for the first round
     *
     * @param leagueSeason
     * @return list of matches
     */
    private List<Match> scheduleFirstRound(LeagueSeason leagueSeason) {
        List<Match> matchesList = new ArrayList<>();

        if (leagueSeason.getTeamsParticipate().size() > 0) {
            List<Team> teamList = new ArrayList<>(leagueSeason.getTeamsParticipate());


            // if number of teams is odd - Whoever is matched against the nullTeam gets a free round.
            if (teamList.size() % 2 != 0) {
                teamList.add(new Team("NullTeam", false, false));
            }

            //creates list without the first team because its place doesn't change while the algorithm
            List<Team> teamListWithoutFirstTeam = new ArrayList<>(teamList);
            teamListWithoutFirstTeam.remove(0);

            int numberOfGamesForEachTeamInRound = teamList.size() - 1;
            Match matchToAdd;

            //Assign matches according to round-robin tournament algorithm - rotates the team clockwise every loop - This is only the FIRST ROUND
            for (int i = 0; i < numberOfGamesForEachTeamInRound; i++) {

                //rotate the team according to the current round
                int teamIndex = i % (teamListWithoutFirstTeam.size());

                //the first team at index 0 never changes, but every round the homeAway method changes.
                if (!teamListWithoutFirstTeam.get(teamIndex).getName().equals("NullTeam")) {
                    if (i % 2 == 0) {
                        matchToAdd = new Match(null, teamList.get(0), teamListWithoutFirstTeam.get(teamIndex), leagueSeason, null); //TODO: ADD STADIUM FROM TEAM
                        addMatchToTeams(matchToAdd, teamList.get(0), teamListWithoutFirstTeam.get(teamIndex));
                    } else {
                        matchToAdd = new Match(null, teamListWithoutFirstTeam.get(teamIndex), teamList.get(0), leagueSeason, null);
                        addMatchToTeams(matchToAdd, teamListWithoutFirstTeam.get(teamIndex), teamList.get(0));
                    }
                    matchesList.add(matchToAdd);
                }

                //runs over the teamsList ( except the first team at index 0 that we already set ), and schedule matches according to their indexes.
                //every loop the indexes change, so that at the end of the loops every team scheduled against all the other teams.
                for (int j = 1; j < teamList.size() / 2; j++) {

                    int firstTeamIndex = (i + j) % teamListWithoutFirstTeam.size();
                    int secondTeamIndex = (i + teamListWithoutFirstTeam.size() - j) % teamListWithoutFirstTeam.size();

                    Team firstTeam = teamListWithoutFirstTeam.get(firstTeamIndex);
                    Team secondTeam = teamListWithoutFirstTeam.get(secondTeamIndex);

                    //don't add the free game while the league has odd number of teams.
                    if (!firstTeam.getName().equals("NullTeam") && !secondTeam.getName().equals("NullTeam")) {

                        //changes between home and away.
                        if (i % 2 == 0) {
                            matchToAdd = new Match(null, firstTeam, secondTeam, leagueSeason, null);
                            addMatchToTeams(matchToAdd, firstTeam, secondTeam);
                        } else {
                            matchToAdd = new Match(null, secondTeam, firstTeam, leagueSeason, null);
                            addMatchToTeams(matchToAdd, secondTeam, firstTeam);
                        }

                        matchesList.add(matchToAdd);
                    }
                }
            }
        }
        return matchesList;
    }

    /**
     * Adds the scheduled match to the required teams
     *
     * @param match
     * @param homeTeam
     * @param awayTeam
     */
    private void addMatchToTeams(Match match, Team homeTeam, Team awayTeam) {
        if (match != null && homeTeam != null && awayTeam != null) {
            homeTeam.setHomeMatches(match);
            awayTeam.setAwayMatches(match);
        }
    }

    /**
     * Schedules every match - sets the Date of each match.
     *
     * @param matches   list of matches that should be schedule
     * @param firstDate the date of the first game.
     * @return list of matches with their Dates.
     */
    private List<Match> setDates(List<Match> matches, Date firstDate) {
        //inits first date of the first game
        if (matches != null && firstDate != null) {
            Date gameDate = new Date(firstDate.getTime());
            gameDate.setHours(10);
            gameDate.setMinutes(0);

            int indexOfMatch = 0;

            if (matches != null && firstDate != null) {

                //Schedules all the matches
                while (indexOfMatch < matches.size()) {

                    //At each day only {gamesPerDay}
                    for (int i = 0; i < gamesPerDay && indexOfMatch < matches.size(); i++) {
                        if (matches.get(indexOfMatch) != null) {
                            matches.get(indexOfMatch).setStartTime(gameDate);
                            gameDate = nextMatchHourAtTheSameDate(gameDate);
                            ++indexOfMatch;
                        }
                    }

                    gameDate = nextMatchDate(gameDate); //next day
                }
            }
        }
        return matches;

    }

    /**
     * This function gets Date and returns the next possible Hour to play a game at the same date as the last schedule game.
     *
     * @param date the last schedule date
     * @return the same date just plus 2 hours,
     */
    private Date nextMatchHourAtTheSameDate(Date date) {
        Date newDate = null;
        if (date != null) {
            //every 2 hours there is a game, while the last game at 22:00 pm - Explanation: Maximum 7 games at one day,
            //therefore while the first game at 10 am plus (7-1)*2 hours between each game - we got the last game is at 22 pm
            newDate = new Date(date.getTime());
            newDate.setHours(date.getHours() + 2);
            newDate.setMinutes(0);
        }
        return newDate;
    }

    /**
     * Moves to the next day, while first game starts at 10:00 am
     *
     * @param date of the last game that was schedule.
     * @return the tomorrow date at 10 am.
     */
    private Date nextMatchDate(Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            newDate = calendar.getTime();
            newDate.setHours(10);
            newDate.setMinutes(0);
        }
        return newDate;
    }

    /**
     * Sets stadiums for matches according to the home team.
     *
     * @param matchList
     * @return list of matches with its stadiums
     */
    private List<Match> setStadiums(List<Match> matchList) {
        if (matchList != null) {
            for (Match matchToSet : matchList) {
                if (matchToSet.getHomeTeam().getStadiums().size() > 0)
                    matchToSet.setStadium(matchToSet.getHomeTeam().getStadiums().get(0));
            }
        }
        return matchList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePolicy that = (GamePolicy) o;
        return numberOfRounds == that.numberOfRounds &&
                gamesPerDay == that.gamesPerDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfRounds, gamesPerDay);
    }

    @Override
    public String toString() {
        return "Rounds=" + numberOfRounds + " , GamesPerDay=" + gamesPerDay;
    }
}
