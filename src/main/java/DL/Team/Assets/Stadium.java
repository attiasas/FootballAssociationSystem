package DL.Team.Assets;

import DL.Team.Team;

import javax.persistence.*;

/**
 * Description: Defines Stadium object - name, capacity and related team    X
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "stadium", query = "SELECT s FROM Stadium s"),
        @NamedQuery(name = "stadiumByName", query = "SELECT s FROM Stadium s WHERE s.name = :name"),
        @NamedQuery(name = "stadiumsByCapacity", query = "SELECT s FROM Stadium s WHERE s.capacity = :capcity"),
        @NamedQuery(name = "stadiumByTeam", query = "SELECT s FROM Stadium s WHERE s.team = :team"),

})

public class Stadium
{
    @Id
    @Column
    private String name;

    @Column
    private int capacity;

    @Column
    @OneToOne (cascade = CascadeType.MERGE)
    private Team team;

    @Override
    public String toString() {
        return "Stadium: " + name + ", Capacity: " + capacity + ".";
    }

    //Constructor
    public Stadium(String name, int capacity, Team team) {
        this.name = name;
        this.capacity = capacity;
        this.team = team;
    }

    public Stadium() {}
}
