package BL.Communication;

import DL.Game.Match;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.TeamOwner;
import DL.Team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicationAssociationManagementStub extends ClientServerCommunication{

    List<Team> teams;
    List<TeamOwner> teamOwners;
    List<Referee> referees;

    public CommunicationAssociationManagementStub(List<Team> teams, List<TeamOwner> teamOwners, List<Referee> referees) {

        this.teams = teams;
        this.teamOwners = teamOwners;
        this.referees = referees;

    }

    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        if (queryName.equals("refereeByFan")) {

            List<Referee> result = new ArrayList<>();

            for(Referee referee : referees)
            {
                if(referee.getFan().equals(parameters.get("fan")))
                    result.add(referee);
            }

            return result;

        }

        else if (queryName.equals("nextMatchesListByReferee")) {

            List<Referee> referees = query("refereeByFan", parameters);
            if (referees == null || referees.isEmpty()) return new ArrayList();

            Referee referee = (Referee) query("refereeByFan", parameters).get(0);

            List<Match> matches = new ArrayList<>();

//            for (Match match : referee.getLinesManMatches())
//            {
//                if (match.getEndTime() == null)
//                    matches.add(match);
//            }
//
//            for (Match match : referee.getMainMatches())
//            {
//                if (match.getEndTime() == null)
//                    matches.add(match);
//            }
            return matches;

        }
        return new ArrayList();
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters) {

        if(queryName.equals("setRefereeActivity")) {
            // query = UPDATE Referee r SET r.active = :active WHERE r.fan = :fan
            List<Referee> result = query("refereeByFan", parameters);

            if (result.isEmpty()) return false;

            Referee referee = result.get(0);

            referee.setActive((boolean)parameters.get("active"));

            return referee.isActive();
        }

        return false;
    }

    @Override
    public boolean insert(Object toInsert) {

        if(toInsert instanceof Team) {
            Team team = (Team) toInsert;
            teams.add(team);
        }

        else if (toInsert instanceof TeamOwner) {
            TeamOwner teamOwner = (TeamOwner) toInsert;
            teamOwners.add(teamOwner);
        }

        else if (toInsert instanceof Referee) {
            Referee referee = (Referee) toInsert;
            referees.add(referee);
        }

        return true;
    }

    @Override
    public boolean delete(Object toDelete) {
        return true;
    }
}
