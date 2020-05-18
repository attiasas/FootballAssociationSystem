package DL.Users;

import DL.Team.Page.Page;

import javax.persistence.*;
import java.util.*;

/**
 * Description:     Represents a Fan in the System. A Fan is the first User object that a registered User gets.
 * ID:              7
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "AllFans", query = "SELECT f From Fan f")
})
public class Fan extends User
{

    @ManyToMany(mappedBy = "FAN_ID",cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private Set<Page> follow;

    public Fan (String userName, String email, String hashedPassword)
    {
        super(userName, email, hashedPassword);
        follow = new HashSet<>();
    }

    public Fan()
    {
        this("", "", "");
    }

    /**
     *
     * @param page
     * @return true if the fan follows the page
     */
    public boolean followPage(Page page)
    {
        if(page == null)
        {
            return false;
        }

        if(follow.add(page))
        {
            if(page.addFollower(this))
            {
                return true;
            }
            follow.remove(page);
        }

        return false;
    }

    public boolean unfollowPage(Page page)
    {
        if(page == null)
        {
            return false;
        }

        if(follow.remove(page))
        {
            if(page.removeFollower(this))
            {
                return true;
            }
            follow.add(page);
        }
        return false;
    }

    public boolean unfollowAllPages()
    {
        Iterator<Page> it = follow.iterator();
        while(it.hasNext())
        {
            Page page = it.next();
            page.removeFollower(this);
        }

        this.follow = new HashSet<>();

        return true;
    }

    public Set<Page> getFollowing()
    {
        return this.follow;
    }

}
