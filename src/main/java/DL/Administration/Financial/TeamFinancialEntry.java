package DL.Administration.Financial;

import DL.Team.Team;
import DL.Users.User;

import javax.persistence.*;

/**
 * Description:     A Financial Entry for a team.
 **/
@NamedQueries(value = {
        @NamedQuery(name = "AllTeamFinancialEntries", query = "Select e From TeamFinancialEntry e"),
        @NamedQuery(name = "TeamFinancialEntries", query = "Select e From TeamFinancialEntry e where e.team=:team")
})

@Entity
@DiscriminatorValue(value = "TeamFinancialEntry")
public class TeamFinancialEntry extends FinancialEntry
{
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    /**
     * Constructor
     * @param source - the user that created the entry
     * @param name - the name (description) of the entry
     * @param amount - amount of entry, positive is income negative is expense
     * @param timeStamp - the date and time that the entry occurred
     * @param team - the team that the entry belongs to
     */
    public TeamFinancialEntry(User source, String name, double amount, long timeStamp, Team team)
    {
        super(source, name, amount, timeStamp);
        this.team = team;
    }

    /**
     * Constructor, timeStamp will be the current date and time
     * @param source - the user that created the entry
     * @param name - the name (description) of the entry
     * @param amount - amount of entry, positive is income negative is expense
     */
    public TeamFinancialEntry(User source, String name, double amount, Team team)
    {
        super(source, name, amount);
        this.team = team;
    }

    /**
     * Default Constructor
     */
    public TeamFinancialEntry()
    {
        super();
        team = null;
    }
}
