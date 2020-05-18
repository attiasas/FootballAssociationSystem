package DL.Team.Page;

import DL.Users.Fan;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: Defines a Page object - a personal page of coach/player/team fan can follow    X
 * ID:              6
 **/


@NamedQueries( value = {
        @NamedQuery(name = "AllPages", query = "SELECT p From Page p"),
        @NamedQuery(name = "PageByFan", query = "SELECT p from Page p WHERE :fan in (followers)")
})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PAGE_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Page implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    public String content;

    @ManyToMany (cascade = CascadeType.ALL)
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

    public boolean isFollower(Fan fan)
    {
        if(followers.contains(fan))
        {
            return true;
        }
        return false;
    }

    public Set<Fan> getFollowers()
    {
        return followers;
    }


}
