package DL.Users;

import DL.Team.Page.Page;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents a Fan in the System. A Fan is the first User object that a registered User gets.
 * ID:              X
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "AllFans", query = "SELECT f From Fan f")
})
public class Fan extends User
{

    @ManyToMany
    private List<Page> follow;


    public Fan (String userName, String email, String hashedPassword)
    {
        super(userName, email, hashedPassword);
        follow = new ArrayList<Page>();
    }

    public Fan()
    {
        this("", "", "");
    }

}
