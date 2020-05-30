package DL.Team.Assets;

import DL.Team.Team;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description: Defines Stadium object - name, capacity and related team    X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "stadium", query = "SELECT s FROM Stadium s"),
        @NamedQuery(name = "stadiumByName", query = "SELECT s FROM Stadium s WHERE s.name = :name"),
        @NamedQuery(name = "stadiumsByCapacity", query = "SELECT s FROM Stadium s WHERE s.capacity = :capcity"),
        @NamedQuery(name = "stadiumsByTeam", query = "SELECT s FROM Stadium s WHERE :team IN (s.teams)"),
        @NamedQuery(name = "setStadiumDetails", query = "UPDATE Stadium s SET s.name = :newName, s.capacity = :newCapacity, s.teams = :teamsList WHERE s.name = :name"),
        @NamedQuery(name = "setStadiumActivity", query = "UPDATE Stadium s SET s.active = :active WHERE s.name = :name"),

})
public class Stadium implements Serializable
{
    @Id
    private String name;

    @Column
    private int capacity;

    @Column
    private boolean active;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Team> teams;


    //Constructor
    public Stadium(String name, int capacity, Team team) {

        if (!isValidStadiumName(name) || capacity <= 0) return;

        this.name = name;
        this.capacity = capacity;
        this.teams = new ArrayList<>();
        this.active = true;
        teams = new ArrayList<>();
        if (team != null)
            teams.add(team);
    }

    public Stadium() {}

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean addTeam(Team team) {

        if (team == null) throw new IllegalArgumentException("Illegal Arguments Insertion: \n Undefined team was inserted.");

        teams.add(team);

        return true;
    }

    public boolean setDetails(String newName, int capacity, List<Team> teamsList) {

        if (!isValidStadiumName(newName) || capacity <= 0 || teamsList == null)
            return false;

        this.name = newName;
        this.capacity = capacity;
        teams = teamsList;

        return true;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    private boolean isValidStadiumName(String name) {

        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }

    @Override
    public String toString() {
        return "Stadium: " + name + ", Capacity: " + capacity + ".";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stadium)) return false;
        Stadium stadium = (Stadium) o;
        return name.equals(stadium.getName());
    }


}
