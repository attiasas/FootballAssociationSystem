package DL.Administration.Financial;

import java.util.List;

/**
 * Description:     This interface Represents a User that can manage and accesses the financial module.
 *
 * UI Info:         * All Users that implements this interface will be shown the FinancialUI.
 **/
public interface FinancialUser
{
    /**
     * Get All the financial entries that this user created
     * @return list of all the entries that was created by the implemented object
     */
    public List<FinancialEntry> getFinancialEntries();
}
