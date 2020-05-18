package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Users.Fan;
import DL.Users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents a referee user
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllReferees", query = "SELECT r From Referee r"),
        @NamedQuery(name = "UpdateRefereeLeagueSeasonList", query = "UPDATE Referee r SET r.leagueSeasons = :newLeagueSeasonList WHERE r.fan.username = : username"),
        @NamedQuery(name = "setRefereeActivity", query = "UPDATE Referee r SET r.active = :active WHERE r.fan = :fan"),
        @NamedQuery(name = "activeRefereeByUser", query = "select r from Referee r where r.fan = :uesr and r.active = true"),
        @NamedQuery(name = "RefereeByFan", query = "SELECT r FROM Referee r WHERE fan = :fan")
})
public class Referee implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @ManyToOne
    private Fan fan;

    @Column
    private boolean active;

    @Column
    private String qualification;
    @OneToMany
    private List<Match> mainMatches;
    @ManyToMany
    private List<Match> linesManMatches;
    @ManyToMany
    private List<LeagueSeason> leagueSeasons;

    public Referee (String qualification, String name, Fan fan, boolean active)
    {
        this.qualification = qualification;
        this.name = name;
        this.fan = fan;
        this.active = active;

        this.mainMatches = new ArrayList<Match>();
        this.linesManMatches = new ArrayList<Match>();
        this.leagueSeasons = new ArrayList<LeagueSeason>();
    }

    public Referee ()
    {
        this("", "", null, true);
    }

    public void addLinesManMatch(Match match) {
        this.linesManMatches.add(match);
    }

    public void addMainMatch(Match match) {
        this.mainMatches.add(match);
    }

    public void addLeagueSeason(LeagueSeason leagueSeason) {
        if (leagueSeason != null)
            this.leagueSeasons.add(leagueSeason);
    }

    public boolean createMatchEvent() {
        return false;
    }

    public String getName()
    {
        return name;
    }

    public Fan getFan()
    {
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

    public List<Match> getMainMatches() {
        return mainMatches;
    }

    public List<Match> getLinesManMatches() {
        return linesManMatches;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Referee))
        {
            return false;
        }
        Referee otherReferee = (Referee) other;
        if (super.equals(other) && otherReferee.getName().equals(this.getName())){
            return true;
        }
        return false;
    }

}

