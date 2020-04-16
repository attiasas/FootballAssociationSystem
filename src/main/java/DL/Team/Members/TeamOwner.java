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
        @NamedQuery(name = "TeamOwner", query = "SELECT to FROM TeamOwner to WHERE to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByTeam", query = "SELECT to FROM TeamOwner to WHERE to.team = :team AND to.active = true AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByTeamUser", query = "SELECT to FROM TeamOwner to WHERE to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerByNominee", query = "SELECT to FROM TeamOwner to WHERE to.nominees = :nominee AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerAddOwnerNominee", query = "UPDATE TeamOwner to SET to.ownerNominee = :ownerNominee WHERE  to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "TeamOwnerAddManageNominee", query = "UPDATE TeamOwner to SET to.manageNominee = :manageNominee WHERE  to.teamUser = :teamUser AND to.team.close = false"),
        @NamedQuery(name = "setTeamToTeamOwner", query = "UPDATE TeamOwner to SET to.team = :team WHERE to.teamUser = :teamUser"),
        @NamedQuery(name = "deactivateTeamOwner", query = "UPDATE TeamOwner to SET to.active = :active WHERE to.teamUser = :teamUser AND to.team.close = false"),
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
    private List<TeamUser> ownerNominee;

    @Column
    @OneToMany (cascade = CascadeType.MERGE)
    private List<TeamManager> manageNominee;

    @Column
    private boolean active;

    public TeamOwner(Team team) {

        if (team == null)
            throw new IllegalArgumentException();

        this.team = team;
        this.ownerNominee = new ArrayList<>();
        this.manageNominee = new ArrayList<>();
        this.active = true;
    }

    public TeamOwner() {}

    @Override
    public List<FinancialEntry> getFinancialEntries() {
        return null;
    }
}
