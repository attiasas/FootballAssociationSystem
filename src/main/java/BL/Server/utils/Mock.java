package BL.Server.utils;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Transactional
@NamedQueries(value = {
    @NamedQuery(name = "Mock.deleteAllRows", query = "DELETE from Mock mock"),
    @NamedQuery(name = "Mock.findAll", query = "SELECT mock FROM Mock mock"),
    @NamedQuery(name = "Mock.ByName", query = "SELECT mock FROM Mock mock WHERE mock.name = :name"),
    @NamedQuery(name = "Mock.ByID", query = "SELECT mock FROM Mock mock WHERE mock.id = :id"),
})

public class Mock implements Serializable {

  @Transient
  public String content;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column
  private String name;

  public Mock(String name) {
    this.name = name;
  }
}
