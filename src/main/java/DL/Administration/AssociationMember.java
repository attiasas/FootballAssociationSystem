package DL.Administration;

import DL.Administration.Financial.AssociationFinancialEntry;
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
@DiscriminatorValue(value = "AssociationMember")
@NamedQueries(value = {
    @NamedQuery(name = "AssociationMembers", query = "Select m From AssociationMember m"),
        @NamedQuery(name = "UpdateAssociationEntries", query = "Update AssociationMember m set m.myEntries=:entries where m=:member")
})
public class AssociationMember extends User implements FinancialUser
{
    @OneToMany(cascade = CascadeType.MERGE)
    private List<AssociationFinancialEntry> myEntries;

    /**
     * Default Constructor
     */
    public AssociationMember()
    {
        myEntries = new ArrayList<>();
    }

    public AssociationMember(String userName, String email, String hashedPassword)
    {
        super(userName, email, hashedPassword);
        myEntries = new ArrayList<>();
    }

    @Override
    public List getFinancialEntries() {
        return myEntries;
    }
}
