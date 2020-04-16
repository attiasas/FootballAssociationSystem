package DL.Users;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Description:     Represents a user in the system
 * ID:              X
 **/
@MappedSuperclass
@NamedQueries( value = {
        @NamedQuery(name = "UserByUsername", query = "SELECT u From User u WHERE u.username = :username"),
        @NamedQuery(name = "UserByUsernameAndPassword", query = "SELECT u FROM User u WHERE u.username = :username AND u.hashedPassword = :hashedPassword")
})
public abstract class User
{
    @Id
    private String username;
    @Column
    private List<String> searches;
    @Column
    private String email;
    @Column
    private String hashedPassword;
    @OneToOne
    private UserPermission userPermission;
    @OneToMany
    private HashMap<Notification, Boolean> notificationsOwner; //maps from notification to a boolean of read or not read
    @OneToMany
    private List<UserComplaint> userComplaintsOwner;




    /**
     *constructor with PermissionList
     * @param userName
     * @param email
     * @param hashedPassword
     * @param permissionList
     */
    public User (String userName, String email, String hashedPassword, List<UserPermission.Permission> permissionList)
    {
        this.username = userName;
        this.searches = new ArrayList<String>();
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userPermission = new UserPermission(permissionList);
        this.notificationsOwner = new HashMap<Notification,Boolean>();
        this.userComplaintsOwner = new ArrayList<UserComplaint>();
    }

    /**
     * constructor without PermissionList
     */
    public User (String userName, String email, String hashedPassword)
    {
        this(userName, email, hashedPassword, new ArrayList<UserPermission.Permission>());
    }

    public User ()
    {
        this("", "", "");
    }

    public void addComplaint(UserComplaint userComplaint)
    {
        this.userComplaintsOwner.add(userComplaint);
    }

    public boolean hasPermission(UserPermission.Permission permission)
    {
        return userPermission.hasPermission(permission);
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }


    @Override
    public String toString ()
    {
        return this.username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

}
