package DL.Administration;

import DL.Users.User;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Description:             This Class Represents a System Admin User.
 *
 * User Responsibilities:   * Managing all the users in the system.
 *                          * Permanently Closing teams.
 *                          * Managing complaints from users.
 *                          * Managing the logs files in the system.
 *                          * Building models for the Recommendation system.
 **/
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "SystemManagers", query = "Select m From SystemManager m")
})
public class SystemManager extends User
{
    /**
     * Default Constructor
     */
    public SystemManager(){}

    public SystemManager(String userName, String email, String hashedPassword)
    {
        super(userName, email, hashedPassword);
    }
}
