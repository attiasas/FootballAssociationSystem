package DL.Team.Members;
import DL.Game.LeagueSeason.Season;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Description:  Defines a user who owes a personal page in the system   X
 * ID:              X
 **/

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PageUser extends TeamUser implements Serializable
{
    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    public UserPage page;

    public PageUser(String name, boolean active, Fan fan, UserPage page, Team team) {
        super(name, active, fan, team);
        this.page = page;
    }

    public PageUser(){}

}