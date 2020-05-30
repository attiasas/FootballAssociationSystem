package DL.Team.Page;

import DL.Users.Fan;
import DL.Users.User;
import com.sun.javafx.beans.IDProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: Defines a Page object - a personal page of coach/player/team fan can follow    X
 * ID:              6
 **/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PAGE_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Page implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    protected String content;

    @ManyToMany(mappedBy = "follow",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<Fan> followers;

    public Page()
    {
        followers = new HashSet<>();
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
