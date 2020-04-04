package DL.Team.Members;
import javax.persistence.*;

import DL.Users.User;
import DL.Users.UserPermission;

import java.util.List;

/**
 * Description:  Defines a TeamUser object - a user related to a team object   X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "teamUser", query = "SELECT tu from TeamUser tu"),
})


@MappedSuperclass
public class TeamUser extends User
{

    public TeamUser(String userName, String email, String hashedPassword, List<UserPermission.Permission> permissionList) {
        super(userName, email, hashedPassword, permissionList);
    }

    public TeamUser(String userName, String email, String hashedPassword) {
        super(userName, email, hashedPassword);
    }

    public TeamUser() {}

}
