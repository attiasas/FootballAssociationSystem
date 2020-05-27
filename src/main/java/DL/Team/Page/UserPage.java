package DL.Team.Page;

import DL.Team.Members.PageUser;
import DL.Team.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Description: Defines a Page object - a personal page of coach/player, a fan can follow    X
 * ID:              X
 **/
@NamedQueries(value = {
        @NamedQuery(name = "UserPage", query = "SELECT up from UserPage up"),
        @NamedQuery(name = "UserPageSetContent", query = "UPDATE UserPage us SET us.content = :content WHERE us.pageUser = :pageUser "),
})
@Entity
@DiscriminatorValue(value = "UserPage")
public class UserPage extends Page implements Serializable
{
    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private PageUser pageUser;

    public UserPage(String content, PageUser pageUser) {

        String err = "";
        if (pageUser == null) {
            err += "User: User doesn't exist. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        this.pageUser = pageUser;
        if (content != null)
            super.content = content;
        else
            super.content = "";
    }

    public UserPage() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPage)) return false;
        UserPage userPage = (UserPage) o;
        return pageUser.equals(userPage.pageUser);
    }

}
