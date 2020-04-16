package DL.Team.Assets;

import DL.Team.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: Defines Stadium object - name, capacity and related team    X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "stadium", query = "SELECT s FROM Stadium s"),
        @NamedQuery(name = "stadiumByName", query = "SELECT s FROM Stadium s WHERE s.name = :name"),
        @NamedQuery(name = "stadiumsByCapacity", query = "SELECT s FROM Stadium s WHERE s.capacity = :capcity"),
        @NamedQuery(name = "stadiumsByTeam", query = "SELECT s FROM Stadium s WHERE :team IN (s.teams) AND s.team.close = false"),
        @NamedQuery(name = "setStadiumDetails", query = "UPDATE Stadium s SET s.name = :newName, s.capacity = :newCapacity, s.teams = :teamsList WHERE s.name = :name"),
        @NamedQuery(name = "deactivateStadium", query = "UPDATE Stadium s SET s.active = false WHERE s.name = :name"),

})

public class Stadium
{
    @Id
    @Column
    private String name;

    @Column
    private int capacity;

    @Column
    private boolean active;

    @Column
    @ManyToMany (cascade = CascadeType.MERGE)
    private List<Team> teams;

    @Override
    public String toString() {
        return "Stadium: " + name + ", Capacity: " + capacity + ".";
    }

    //Constructor
    public Stadium(String name, int capacity, Team team) {

        if (name == null || capacity <= 0 || team == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.capacity = capacity;
        this.teams = new ArrayList<>();
        this.active = true;
        teams.add(team);
    }

    public Stadium() {}

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean setDetails(String newName, int capacity, List<Team> teamsList) {

        if (!isValidStadiumName(newName) || capacity <= 0 || teamsList == null)
            return false;

        this.name = newName;
        this.capacity = capacity;
        this.teams = teamsList;

        return true;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean deactivateStadium() {
        this.active = false;
        return true;
    }

    public boolean isActive() {
        return active;
    }

    private boolean isValidStadiumName(String name) {

        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }
}
