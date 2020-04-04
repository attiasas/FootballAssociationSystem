package DL.Team.Members;
import DL.Team.Page.UserPage;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.util.List;

/**
 * Description:  Defines a user who owes a personal page in the system   X
 * ID:              X
 **/
@Entity
@MappedSuperclass
public abstract class PageUser extends TeamUser
{
    @Column
    @OneToOne(cascade = CascadeType.MERGE)
    public UserPage page;

    public PageUser(String userName, String email, String hashedPassword, List<UserPermission.Permission> permissionList, UserPage page) {
        super(userName, email, hashedPassword, permissionList);
        this.page = page;
    }

    public PageUser(String userName, String email, String hashedPassword, UserPage page) {
        super(userName, email, hashedPassword);
        this.page = page;
    }

    public PageUser(){}

}
