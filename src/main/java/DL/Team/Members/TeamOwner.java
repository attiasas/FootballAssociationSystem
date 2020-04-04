package DL.Team.Members;
import DL.Administration.Financial.FinancialEntry;
import DL.Administration.Financial.FinancialUser;
import DL.Team.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:  Defines a TeamOwner object - Owner of a specific team  X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "TeamOwner", query = "SELECT to FROM TeamOwner to"),
        @NamedQuery(name = "TeamOwnerByTeam", query = "SELECT to FROM TeamOwner to WHERE to.team = :team"),
        @NamedQuery(name = "TeamOwnerByTeamUser", query = "SELECT to FROM TeamOwner to WHERE to.teamUser = :teamUser"),
        @NamedQuery(name = "TeamOwnerByNominee", query = "SELECT to FROM TeamOwner to WHERE to.nominees = :nominee"),
        @NamedQuery(name = "TeamOwnerAddNominee", query = "UPDATE TeamOwner to SET to.nominees = :newNomineesList WHERE  to.teamUser = :teamUser"),
        @NamedQuery(name = "setStatus", query = "UPDATE Team t SET t.close = :close WHERE t.name = :name "),
        //TODO: add to nominees new nominee
})


public class TeamOwner implements FinancialUser
{
    @Id
    @Column
    @OneToOne (cascade = CascadeType.MERGE)
    private TeamUser teamUser;


    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    @Column
    @OneToMany (cascade = CascadeType.MERGE)
    private List<TeamUser> nominees;

    public TeamOwner(Team team) {
        this.team = team;
        this.nominees = new ArrayList<>();
    }

    public TeamOwner() {}

    @Override
    public List<FinancialEntry> getFinancialEntries() {
        return null;
    }
}
