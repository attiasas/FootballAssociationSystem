package DL.Users;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:     Represents a notification in the system
 * ID:              X
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "Notification", query = "SELECT n From Notification n WHERE n.id = :id")
})
public class Notification implements Serializable
{

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String msg;


    public Notification(String msg)
    {
        this.msg = msg;
    }

    public Notification()
    {
        this.msg = "";
    }
}
