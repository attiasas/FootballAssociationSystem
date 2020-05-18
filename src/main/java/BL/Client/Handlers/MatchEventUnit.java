package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.SystemRequest;
import DL.Game.Match;
import DL.Game.MatchEvents.*;
import DL.Game.Referee;
import DL.Team.Members.Player;
import DL.Users.User;

import java.util.*;

/**
 * Description:     X
 * ID:              X
 **/
public class MatchEventUnit
{
    private ClientServerCommunication communication;
    private Referee cachedReferee;

    /**
     * Constructor
     * @param communication - Object That Connects to the DataBase
     */
    public MatchEventUnit(ClientServerCommunication communication)
    {
        this.communication = communication;
    }

    private Map<String,Object> mapOf(String k1, Object v1)
    {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put(k1,v1);
        return parameters;
    }

    private Map<String,Object> mapOf(String k1, Object v1, String k2, Object v2)
    {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put(k1,v1);
        parameters.put(k2,v2);
        return parameters;
    }

    /**
     * Check if user is referee, and fetch info from server storing in cache
     * @param user - user to check if he has an active referee
     * @return - true if user has active referee, false otherwise
     */
    private boolean getRefereeFromServer(User user)
    {
        if(user == null) return false;
        if(cachedReferee != null && cachedReferee.getFan().equals(user)) return true; // cached user, nothing to get

        // new user, fetch info from server
        List<Referee> queryResult = communication.query("activeRefereeByUser",mapOf("user",user));

        if(queryResult == null || queryResult.isEmpty()) return false; // not a team owner or exception

        // only one can be returned from the query
        cachedReferee = queryResult.get(0);

        return true;
    }

    /**
     * Get all the active matches (that the eventLog can be change) that the referee is the main referee
     * @param user - user that has an active referee
     * @return - list of all the active matches that the referee is the main referee, null otherwise
     */
    public List<Match> getActiveMatches(User user)
    {
        if(!getRefereeFromServer(user)) return null;

        List<Match> result = new ArrayList<>();

        // filter only active matches
        for(Match match : cachedReferee.getMainMatches())
        {
            if(match.isMatchEventPeriodOver()) continue;

            result.add(match);
        }

        return result;
    }

    /**
     * check if the given players are active and there teams are playing in the match
     * @param match
     * @param players
     * @return
     */
    private boolean isActivePlayerOfMatch(Match match, Player... players)
    {
        boolean result = match != null && players != null;

        for(int i = 0; i < players.length && result; i++)
        {
            if(!players[i].isActive()) return false;
            result = match.getHomeTeam().getPlayers().contains(players[i]) ? true : match.getAwayTeam().getPlayers().contains(players[i]);
        }

        return result;
    }

    /**
     * Add Yellow Card Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param player - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addYellowCard(User user, Match match, Player player, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;
        if(!isActivePlayerOfMatch(match,player)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        YellowCard yellowCard = new YellowCard(cachedReferee,matchEventLog, matchTime,player);
        matchEventLog.addMatchEvent(yellowCard);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(yellowCard));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Red Card Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param player - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addRedCard(User user, Match match, Player player, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;
        if(!isActivePlayerOfMatch(match,player)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        RedCard redCard = new RedCard(cachedReferee,matchEventLog, matchTime,player);
        matchEventLog.addMatchEvent(redCard);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(redCard));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Goal Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param player - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addGoal(User user, Match match, Player player, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;
        if(!isActivePlayerOfMatch(match,player)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        Goal goal = new Goal(cachedReferee,matchEventLog, matchTime,player);
        matchEventLog.addMatchEvent(goal);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(goal));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Injury Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param player - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addInjury(User user, Match match, Player player, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;
        if(!isActivePlayerOfMatch(match,player)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        Injury injury = new Injury(cachedReferee,matchEventLog, matchTime,player);
        matchEventLog.addMatchEvent(injury);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(injury));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Offside Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param player - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addOffside(User user, Match match, Player player, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;
        if(!isActivePlayerOfMatch(match,player)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        Offside offside = new Offside(cachedReferee,matchEventLog, matchTime,player);
        matchEventLog.addMatchEvent(offside);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(offside));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Foul Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param injuredPlayer - player that is involved in the event
     * @param foulPlayer - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addFoul(User user, Match match, Player injuredPlayer, Player foulPlayer, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0 || injuredPlayer.equals(foulPlayer)) return false;
        if(!isActivePlayerOfMatch(match,injuredPlayer,foulPlayer)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        Foul foul = new Foul(cachedReferee,matchEventLog,matchTime,injuredPlayer,foulPlayer);
        matchEventLog.addMatchEvent(foul);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(foul));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add PlayerChange Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param outPlayer - player that is involved in the event
     * @param inPlayer - player that is involved in the event
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addPlayerChange(User user, Match match, Player outPlayer, Player inPlayer, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0 || outPlayer.equals(inPlayer)) return false;
        if(!isActivePlayerOfMatch(match,outPlayer,inPlayer)) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        PlayerChange playerChange = new PlayerChange(cachedReferee,matchEventLog,matchTime,outPlayer,inPlayer);
        matchEventLog.addMatchEvent(playerChange);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(playerChange));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add Penalty Kick Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addPenaltyKick(User user, Match match, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        PenaltyKick penaltyKick = new PenaltyKick(cachedReferee,matchEventLog,matchTime);
        matchEventLog.addMatchEvent(penaltyKick);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(penaltyKick));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add StoppageTime Event to the given match and player
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param matchTime - the time (minute) of the game that the event happened
     * @param timeAdd - the time (minute)  added to the game
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addStoppageTime(User user, Match match, int matchTime, int timeAdd)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || matchTime < 0 || timeAdd < 0) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        StoppageTime stoppageTime = new StoppageTime(cachedReferee,matchEventLog,matchTime,timeAdd);
        matchEventLog.addMatchEvent(stoppageTime);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(stoppageTime));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));

        return communication.transaction(requests);
    }

    /**
     * Add EndGame Event to the given match and player, and calculate the time for the endGame (and update it)
     * @param user - user that is the main referee of the given match
     * @param match - match that the event happened in
     * @param matchTime - the time (minute) of the game that the event happened
     * @return - true if the event was added successfully, false otherwise
     */
    public boolean addEndGame(User user, Match match, int matchTime)
    {
        // Validate inputs and match is active and the user can update his events
        List<Match> matches = getActiveMatches(user);
        if(match == null || matches == null || !matches.contains(match) || match.isMatchEventPeriodOver() || matchTime < 0) return false;

        // Add Event
        List<SystemRequest> requests = new ArrayList<>();
        EventLog matchEventLog = match.getMyEventLog();

        // end game and set end time
        EndGame endGame = new EndGame(cachedReferee,matchEventLog,matchTime);
        matchEventLog.addMatchEvent(endGame);
        Date endTime = new Date(match.getStartTime().getTime() + (matchTime * 1000 * 60)); // start time + matchTime (minutes) * 1000 * 60 (convert to milli sec)
        match.setEndTime(endTime);

        // Notify
        // TODO: Add notification to users if game is not ended (real time notification)

        // Update Server
        requests.add(SystemRequest.insert(endGame));
        requests.add(SystemRequest.update("UpdateMatchEventLog",mapOf("eventLog",matchEventLog, "match",match)));
        requests.add(SystemRequest.update("UpdateMatchEndTime",mapOf("endTime",endTime, "match",match)));

        return communication.transaction(requests);
    }
}
