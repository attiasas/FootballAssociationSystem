package DL.Team.Members;
import DL.Game.LeagueSeason.Season;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserPermission;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Description:  Defines a user who owes a personal page in the system   X
 * ID:              X
 **/

@Entity
public abstract class PageUser extends TeamUser implements Serializable
{
    @OneToOne(cascade = {CascadeType.ALL})
    public UserPage page;

    public PageUser(String name, boolean active, Fan fan, UserPage page, Team team) {
        super(name, active, fan, team);
        this.page = page;
    }

    public PageUser(){}

}