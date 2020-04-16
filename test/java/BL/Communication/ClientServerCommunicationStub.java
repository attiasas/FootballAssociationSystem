package java.BL.Communication;

import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a Stub of ClientServerCommunication class in order to test the leagueSeasonUnit
 */
public class ClientServerCommunicationStub extends ClientServerCommunication {

    private List<League> leagues;
    private List<Season> seasons;
    private List<LeagueSeason> leagueSeasons;

    public ClientServerCommunicationStub() {
        this.leagues = new ArrayList<>();
        this.seasons = new ArrayList<>();
        this.leagueSeasons = new ArrayList<>();
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters) {
        switch (queryName) {
            case "GetAllLeagues":
                return leagues;
            case "GetAllSeasons":
                return seasons;
            case "GetAllLeagueSeasons":
                return leagueSeasons;
            default:
                return null;
        }
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters) {
        switch (queryName) {
            case "UpdateScorePolicy":
                Object scorePolicy, league, season;
                for (Map.Entry<String, Object> m : parameters.entrySet()) {
                    if (m.getKey().equals("newScorePolicy")) {
                        scorePolicy = m.getValue();
                    } else if (m.getKey().equals("league")) {
                        league = m.getValue();
                    } else if (m.getKey().equals("season")) {
                        season = m.getValue();
                    }
                }
        }
        return false;
    }

    @Override
    public boolean insert(Object toInsert) {
        if (toInsert instanceof League) {
            if (checkIfObjectExists(toInsert, leagues) == -1) {
                leagues.add((League) toInsert);
                return true;
            }
        } else if (toInsert instanceof Season) {
            if (checkIfObjectExists(toInsert, seasons) == -1) {
                seasons.add((Season) toInsert);
                return true;
            }
        } else if (toInsert instanceof LeagueSeason) {
            if (checkIfObjectExists(toInsert, leagueSeasons) == -1) {
                leagueSeasons.add((LeagueSeason) toInsert);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Object toDelete) {
        int index = -1;
        if (toDelete instanceof League) {
            if ((index = checkIfObjectExists(toDelete, leagues)) > -1) {
                leagues.remove(index);
                return true;
            }
        } else if (toDelete instanceof Season) {
            if ((index = checkIfObjectExists(toDelete, seasons)) > -1) {
                seasons.remove(index);
                return true;
            }
        } else if (toDelete instanceof LeagueSeason) {
            if ((index = checkIfObjectExists(toDelete, leagueSeasons)) > -1) {
                leagueSeasons.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * checks if two objects are the same and returns their index
     *
     * @param toCheck    object to find in the list
     * @param objectList list of objects
     * @return the required object index
     */
    private int checkIfObjectExists(Object toCheck, List objectList) {
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i).equals(toCheck))
                return i;
        }
        return -1;
    }
}
