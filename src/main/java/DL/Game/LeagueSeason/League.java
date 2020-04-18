package DL.Game.LeagueSeason;

import javax.persistence.*;
import java.util.Objects;

/**
 * Description:     This class represents a league.
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetLeague", query = "SELECT l From League l WHERE l.name = :name"),
        @NamedQuery(name = "GetAllLeagues", query = "SELECT l From League l")
})
public class League {

    @Id
    private String name;

    /**
     * Ctor
     * @param name
     */
    public League(String name) {
        if (name == null || name.equals(""))
            this.name = "DefaultName";
        else
            this.name = name;
    }

    /**
     * Default ctor
     */
    public League() {
        this("");
    }

    /**
     * Getter
     * @return league name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return Objects.equals(name, league.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "League{" +
                "name='" + name + '\'' +
                '}';
    }

}
