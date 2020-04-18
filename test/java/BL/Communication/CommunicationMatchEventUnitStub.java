package BL.Communication;

import DL.Game.Match;
import DL.Game.Referee;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By: Assaf, On 18/04/2020
 * Description:
 */
public class CommunicationMatchEventUnitStub extends ClientServerCommunication
{
    private List<Match> matches;
    private List<Referee> referees;

    private List<String> queryToList;

    public CommunicationMatchEventUnitStub(List<Match> matches, List<Referee> referees) {
        this.matches = matches;
        this.referees = referees;
    }

    public CommunicationMatchEventUnitStub()
    {
        matches = new ArrayList<>();
        referees = new ArrayList<>();
    }

    private void QueriesInUse()
    {
        queryToList = new ArrayList<>();
        queryToList.add("activeRefereeByUser"); // select r from Referee where r.fan = :uesr and r.active = true
        queryToList.add("UpdateMatchEventLog"); // update Match m set m.myEventLog = :eventLog where m = : match
        queryToList.add("updatePlayerEvents"); // update Player p set p.playerEvents = :playerEvents where p =: player
        queryToList.add("UpdateMatchEndTime"); // update Match m set m.endTime =: endTime where m =: match
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("activeRefereeByUser"))
        {
            // query = select r from Referee where r.fan = :uesr and r.active = true
            List<Referee> result = new ArrayList<>();

            for(Referee referee : referees)
            {
                if(referee.isActive() && referee.getFan().equals(parameters.get("user"))) result.add(referee);
            }

            return result;
        }

        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters)
    {
        return true;
    }

    @Override
    public boolean insert(Object toInsert)
    {
        return true;
    }

    @Override
    public boolean delete(Object toDelete)
    {
        return true;
    }

    @Override
    public boolean transaction(List<SystemRequest> requests)
    {
        boolean res = true;

        for(int i = 0; i < requests.size() && res; i++)
        {
            SystemRequest current = requests.get(i);
            switch (current.type)
            {
                case Insert: res = insert(current.data); break;
                case Delete: res = delete(current.data); break;
                case Update: res = update(current.queryName,(Map<String,Object>)current.data); break;
                default: res = false;
            }
        }

        return res;
    }
}
