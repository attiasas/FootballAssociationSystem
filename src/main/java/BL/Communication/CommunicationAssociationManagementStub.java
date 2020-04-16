package BL.Communication;

import DL.Team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicationAssociationManagementStub extends ClientServerCommunication{

    List<Team> teams;

    public CommunicationAssociationManagementStub(List<Team> teams) {

        this.teams = teams;

    }

    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        return new ArrayList();
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters) {
        return true;
    }

    @Override
    public boolean insert(Object toInsert) {

        if(toInsert instanceof Team)
        {
            Team team = (Team) toInsert;
            teams.add(team);
        }

        return true;
    }

    @Override
    public boolean delete(Object toDelete) {
        return true;
    }
}
