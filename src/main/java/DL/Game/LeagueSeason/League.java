package DL.Game.LeagueSeason;

import javax.persistence.*;

/**
 * Description:     X
 * ID:              X
 **/

@Entity
@NamedQueries( value = {
        @NamedQuery(name = "League", query = "SELECT l From League l WHERE l.name = :name")
})
public class League
{

    @Id
    private String name;

    public League(String name)
    {
        this.name = name;
    }

    public League()
    {
        this("");
    }


}
