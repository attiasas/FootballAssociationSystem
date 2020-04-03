package DL.Administration;

import DL.Administration.Financial.FinancialEntry;
import DL.Administration.Financial.FinancialUser;
import DL.Users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:             This Class Represents a Football Association Member
 *
 * User Responsibilities:   * Managing the Association Financial records and Budget exceptions of teams.
 *                          * Managing the the structures, participants and policies of matches in all the seasons of all the leagues.
 *                          * Assigning Referees to the associations and matches.
 **/
@Entity
@NamedQueries(value = {
    @NamedQuery(name = "AssociationMembers", query = "Select m From AssociationMember m"),
        @NamedQuery(name = "UpdateAssociationEntries", query = "Update AssociationMember m set m.myEntries=:entries where m=:member")
})
public class AssociationMember extends User implements FinancialUser
{
    @OneToMany(cascade = CascadeType.MERGE)
    private List<FinancialEntry> myEntries;

    /**
     * Default Constructor
     */
    public AssociationMember()
    {
        myEntries = new ArrayList<>();
    }

    @Override
    public List<FinancialEntry> getFinancialEntries() {
        return myEntries;
    }
}