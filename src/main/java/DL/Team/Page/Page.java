package DL.Team.Page;

import DL.Users.Fan;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: Defines a Page object - a personal page of coach/player/team fan can follow    X
 * ID:              X
 **/

@MappedSuperclass
@NamedQueries( value = {
        @NamedQuery(name = "AllFans", query = "SELECT f From Fan f")
})

public abstract class Page
{

    @Column
    public String content;

    @ManyToMany (cascade = CascadeType.MERGE)
    Set<Fan> followers;

    public Page()
    {
        followers = new HashSet<Fan>();
    }

    public boolean addFollower(Fan fan)
    {
        return this.followers.add(fan);
    }

    public boolean removeFollower(Fan fan)
    {
        return followers.remove(fan);
    }

}
