package BL.Server.utils;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;


@Table(name = "MOCK")
@Entity
@IdClass(Mock.EntryPK.class)
public class Mock implements Serializable {

    /**
     * For Composite Primary Key
     */
    public static class EntryPK implements Serializable{
        public int id;
        public int id2;
    }

    @Id
    @GeneratedValue
    private int id;
    @Id
    @GeneratedValue
    private int id2;
    @Column(name = "USER_NAME", unique = true)
    private String username;
    @Column(name = "ACTIVE")
    private Boolean active = true;
    @Column(name = "name_check")
    private String name;

    @OneToMany(targetEntity = MockTemp.class,orphanRemoval = true,mappedBy = "mock",cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MockTemp> mockTemps;

    public void addMock(List<MockTemp> m ){
        mockTemps = new ArrayList<>();
        mockTemps.addAll(m);
    }
}

