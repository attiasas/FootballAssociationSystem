package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents a referee user
 * ID:              X
 **/

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "AllReferees", query = "SELECT r From Referees r"),
        @NamedQuery(name = "UpdateRefereeLeagueSeasonList", query = "UPDATE Referee r SET r.leagueSeasons = :newLeagueSeasonList WHERE  r.username = : username")
})
public class Referee extends User {

    @Column
    private String qualification;
    @OneToMany
    private List<Match> mainMatches;
    @ManyToMany
    private List<Match> linesManMatches;
    @ManyToMany
    private List<LeagueSeason> leagueSeasons;


    public Referee(String qualification, String userName, String email, String hashedPassword) {
        super(userName, email, hashedPassword);
        this.qualification = qualification;
        this.mainMatches = new ArrayList<Match>();
        this.linesManMatches = new ArrayList<Match>();
        this.leagueSeasons = new ArrayList<>();
    }

    public Referee() {
        this("", "", "", "");
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

    public List<LeagueSeason> getLeagueSeasons() {
        return leagueSeasons;
    }

}
