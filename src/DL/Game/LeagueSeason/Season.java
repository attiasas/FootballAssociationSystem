package DL.Game.LeagueSeason;

import javax.persistence.*;

/**
 * Description:     Represents a Season
 * ID:              X
 **/

@Entity
@NamedQueries( value = {
        @NamedQuery(name = "Season", query = "SELECT s From Season s WHERE s.year = :year")
})
public class Season
{

    @Id
    private int year;

    public Season (int year)
    {
        this.year = year;
    }

    public Season()
    {
        this(0);
    }

}
