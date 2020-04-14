package DL.Team.Members;
import DL.Team.Page.UserPage;
import DL.Users.Fan;
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

    public PageUser(String name, boolean active, Fan fan, UserPage page) {
        super(name, active, fan);
        this.page = page;
    }

    public PageUser(){}

}