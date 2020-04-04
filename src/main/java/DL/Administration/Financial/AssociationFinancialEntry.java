package DL.Administration.Financial;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:     A Financial Entry for the football association.
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AssociationFinancialEntries", query = "Select e From AssociationFinancialEntry e")
})
public class AssociationFinancialEntry extends FinancialEntry
{

}
