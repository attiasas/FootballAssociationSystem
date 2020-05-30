package DL.Game.LeagueSeason;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Description:     Represents a Season
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetSeason", query = "SELECT s From Season s WHERE s.year = :year"),
        @NamedQuery(name = "GetAllSeasons", query = "SELECT s From Season s")
})
public class Season implements Serializable {

    @Id
    @Column
    private int year;

    /**
     * Ctor
     *
     * @param year of the season
     */
    public Season(int year) {
        if (year < 1950)
            this.year = 1950;
        else
            this.year = year;
    }

    /**
     * Default ctor
     */
    public Season() {
        this(1950);
    }

    /**
     * getter
     * @return season year
     */
    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return year == season.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }

    @Override
    public String toString() {
        return "SeasonYear{" + year + '}';
    }
}
