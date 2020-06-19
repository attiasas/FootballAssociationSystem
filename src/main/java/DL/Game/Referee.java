package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Users.Fan;
import DL.Users.User;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:     Represents a referee user
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllReferees", query = "SELECT r From Referee r"),
        @NamedQuery(name = "UpdateRefereeLeagueSeasonList", query = "UPDATE Referee r SET r.leagueSeasons = :newLeagueSeasonList WHERE r.fan.username = : username"),
        @NamedQuery(name = "setRefereeActivity", query = "UPDATE Referee r SET r.active = :active WHERE r.fan = :fan"),
        @NamedQuery(name = "activeRefereeByUser", query = "select r from Referee r where r.fan = :user and r.active = true"),
        @NamedQuery(name = "RefereeByFan", query = "SELECT r FROM Referee r WHERE fan = :fan")
})
public class Referee implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @OneToOne
    private Fan fan;

    @Column
    private boolean active;

    @Column
    private String qualification;

    @ManyToMany(mappedBy = "referees")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Match> matches;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<LeagueSeason> leagueSeasons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Referee(String qualification, String name, Fan fan, boolean active) {
        this.qualification = qualification;
        this.name = name;
        this.fan = fan;
        this.active = active;
        this.matches = new ArrayList<>();
        this.leagueSeasons = new ArrayList<>();
    }

    public Referee() {
        this("", "", null, true);
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public boolean addLeagueSeason(LeagueSeason leagueSeason) {
        if (leagueSeason != null && !checkIfObjectExists(leagueSeason,leagueSeasons)) {
            this.leagueSeasons.add(leagueSeason);
            return true;
        }
        return false;
    }

    public boolean createMatchEvent() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Fan getFan() {
        return fan;
    }

    public List<LeagueSeason> getLeagueSeasons() {
        return leagueSeasons;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public boolean isMainReferee()
    {
        return "main".equals(qualification.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Referee)) {
            return false;
        }
        Referee otherReferee = (Referee) other;
        if (otherReferee.getId() == (this.getId())) {
            return true;
        }
        return false;
    }

    /**
     * checks if the object already exists
     *
     * @param toCheck    object to find in the list
     * @param objectList list of objects
     * @return true if the object exists
     */
    private boolean checkIfObjectExists(Object toCheck, List objectList) {
        for (Object listObject : objectList) {
            if (listObject.equals(toCheck))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fan, active, qualification, matches, leagueSeasons);
    }

    @Override
    public String toString() {
        return "Referee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

