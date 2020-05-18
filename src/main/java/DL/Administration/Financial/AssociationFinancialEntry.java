package DL.Administration.Financial;

import DL.Administration.AssociationMember;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     A Financial Entry for the football association.
 **/
@NamedQueries(value = {
        @NamedQuery(name = "AssociationFinancialEntries", query = "Select e From AssociationFinancialEntry e")
})
@Entity
@DiscriminatorValue(value = "AssociationFinancialEntry")
public class AssociationFinancialEntry extends FinancialEntry
{

}
