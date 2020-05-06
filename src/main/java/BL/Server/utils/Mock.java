package BL.Server.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;


@NamedQueries(value = {
        @NamedQuery(name = "Mock.deleteAllRows", query = "DELETE from Mock mock"),
        @NamedQuery(name = "Mock.findAll", query = "SELECT mock FROM Mock mock"),
        @NamedQuery(name = "Mock.ByName", query = "SELECT mock FROM Mock mock WHERE mock.name = :name"),
        @NamedQuery(name = "Mock.ByID", query = "SELECT mock FROM Mock mock WHERE mock.id = :id"),
})

@Setter
@Getter
@NoArgsConstructor
@Transactional
@Table(name = "MOCK")
@Entity
@SuppressWarnings("serial")
public class Mock implements Serializable {
    @Transient
    public String draft;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;
    @Column(name = "GENDER", nullable = false)
    private Long gender;
    @Temporal(TemporalType.DATE)
    @Column(name = "DOB", nullable = false)
    private Date dob;
    @Column(name = "ACTIVE")
    private Boolean active = true;
    @Column
    private String name;

    public Mock(String name) {
        this.name = name;
    }
}
