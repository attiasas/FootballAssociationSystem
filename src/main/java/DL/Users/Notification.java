package DL.Users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Column
    private Date creationDate;
//    @ManyToMany(mappedBy = "notificationsOwner")
//    List<User> owners;

    public Notification(String msg)
    {
        this.msg = msg;
        this.creationDate = new Date();
    }

    public Notification()
    {
        this.msg = "";
    }

    public String getMsg()
    {
        return this.msg;
    }

    @Override
    public String toString()
    {
        return this.msg;
    }

    public Date getCreationDate()
    {
        return this.creationDate;
    }

//    @Override
//    public boolean equals(Object other)
//    {
//        return this.msg == ((Notification)other).msg;
//    }
}
