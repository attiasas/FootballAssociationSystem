package DL.Team.Page;

import DL.Team.Members.PageUser;
import DL.Team.Team;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: Defines a Page object - a personal page of coach/player, a fan can follow    X
 * ID:              X
 **/
@NamedQueries(value = {
        @NamedQuery(name = "UserPage", query = "SELECT up from UserPage up"),
        @NamedQuery(name = "UserPageSetContent", query = "UPDATE UserPage us SET us.content = :content WHERE us.pageUser = :pageUser "),

})
@Entity
@IdClass(UserPage.EntryPK.class)
@DiscriminatorValue(value = "UserPage")
public class UserPage extends Page implements Serializable
{
    /**
     * For Composite Primary Key
     */
    public class EntryPK implements Serializable {
        public long id;
        public PageUser pageUser;
    }

    @Id
    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.MERGE})
    private PageUser pageUser;

    public UserPage(String content, PageUser pageUser) {

        if (pageUser == null || content == null) throw new IllegalArgumentException();

        this.pageUser = pageUser;
        super.content = content;
    }

    public UserPage() {

    }

}
