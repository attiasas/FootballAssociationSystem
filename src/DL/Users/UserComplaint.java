package DL.Users;

import javax.persistence.*;

/**
 * Description:     X
 * ID:              X
 **/
@Entity
@NamedQueries( value = {
        @NamedQuery(name = "UserComplaint", query = "SELECT uc From UserComplaint uc WHERE uc.id = :id")
})
public class UserComplaint {

    @Id
    @GeneratedValue
    private int id;
    @Column
    private String msg;
    @Column
    private String response;
    @ManyToOne
    private User owner;


    /**
     * Constructor without response
     *
     * @param owner
     * @param msg
     * @param response
     */
    public UserComplaint(User owner, String msg, String response)
    {
        this.owner = owner;
        this.msg = msg;
        this.response = response;
    }

    /**
     * Constructor without response
     *
     * @param owner
     * @param msg
     */
    public UserComplaint(User owner, String msg)
    {
        this(owner, msg, null);
    }

    public UserComplaint()
    {
        this(null, "");
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public String toString ()
    {
        return "User: "+ this.owner + ", Complaint: " + this.msg + ", Response: " + response;
    }

}
