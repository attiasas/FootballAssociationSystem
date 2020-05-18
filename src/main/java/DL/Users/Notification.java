package DL.Users;

import javax.persistence.*;

/**
 * Description:     Represents a notification in the system
 * ID:              X
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "Notification", query = "SELECT n From Notification n WHERE n.id = :id")
})
public class Notification
{

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String msg;
    @Column
    private boolean read;

    public Notification(String msg, boolean read)
    {
        this.msg = msg;
        this.read = read;
    }

    public Notification (String msg)
    {
        this(msg, false);
    }

    public Notification ()
    {
        this(null, false);
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }

}
