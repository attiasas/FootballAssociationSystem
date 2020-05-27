package DL.Users;
import BL.Server.utils.StringListConverter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


/**
 * Description:     Represents a user in the system
 * ID:              X
 **/
@NamedQueries( value = {
        @NamedQuery(name = "UserByUserName", query = "SELECT u From User u WHERE u.username = :username"),
        @NamedQuery(name = "UserByUsernameAndPassword", query = "SELECT u FROM User u WHERE u.username = :username AND u.hashedPassword = :password"),
        @NamedQuery(name = "UpdateUserPermission", query = "update User u set u.userPermission = :permission where u = :user"),
        @NamedQuery(name = "DeactivateUser", query = "UPDATE User u SET u.active = false where u= :user"),
        @NamedQuery(name = "ActivateUser", query = "UPDATE User u SET u.active = true where u=:user"),
})
@Entity
@Table(name = "UserTable")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements Serializable
{
//    @Id

    @Id
    private String username;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column
    private boolean active;


    //    @ElementCollection
    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> searches;
    @Column
    private String email;
    @Column
    private String hashedPassword;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinTable(name="UserToUserPermission", joinColumns = {@JoinColumn(name = "username")}, inverseJoinColumns = {@JoinColumn(name="id")})
    private UserPermission userPermission;

    //    @OneToMany(cascade = CascadeType.ALL)
//    @JoinTable(name="UserToNotification", joinColumns = {@JoinColumn(name = "username")}, inverseJoinColumns = {@JoinColumn(name="id")})
    @ElementCollection
    private Map<Notification, Boolean> notificationsOwner; //maps from notification to a boolean of read or not read

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
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
        this.active = true;
        this.searches = new ArrayList<String>();
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userPermission = new UserPermission(permissionList);
        this.notificationsOwner = new HashMap<Notification,Boolean>();
        this.userComplaintsOwner = new ArrayList<UserComplaint>();
    }

    public User(User other)
    {
        this(other.username,other.email,other.hashedPassword);

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


    public boolean hasPermission(UserPermission.Permission permission)
    {
        return userPermission.hasPermission(permission);
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getHashedPassword()
    {
        return this.hashedPassword;
    }

    public List<UserComplaint> getUserComplaintsOwner()
    {
        return this.userComplaintsOwner;
    }

    public boolean addUserComplaint(UserComplaint userComplaint)
    {
        if(userComplaint == null)
        {
            return false;
        }
        this.userComplaintsOwner.add(userComplaint);
        return true;
    }
    public String getEmail() {
        return email;
    }


    @Override
    public String toString ()
    {
        return this.username;
    }

    public void setUserPermission(UserPermission userPermission)
    {
        this.userPermission = userPermission;
    }

    public UserPermission getUserPermission() { return userPermission; }
    @Override
    public boolean equals(Object other)
    {
        if(other == null || !(other instanceof User))
        {
            return false;
        }
        User otherUser = (User)other;
        if(otherUser.username.equals(this.username))
        {
            return true;
        }
        return false;
    }

    public boolean setActive(boolean active)
    {
        this.active = active;
        return true;
    }
    public boolean getActive()
    {
        return active;
    }

    /**
     * add a totification to the users notifications list.
     * the notification is added as not read
     * @param notification
     * @return true if
     */
    public boolean addNotification (Notification notification)
    {
        if(notificationsOwner.containsKey(notification))
        {
            // the user already has this notification
            return false;
        }

        this.notificationsOwner.put(notification, false);
        return true;
    }

    public boolean markAllNotificationsAsRead()
    {
        Iterator it = this.notificationsOwner.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            Notification notification = (Notification)pair.getKey();
            markNotificationAsRead(notification);
        }
        return true;
    }

    public boolean markNotificationAsRead(Notification notification)
    {
        this.notificationsOwner.put(notification, true);
        return true;
    }

    public Map<Notification, Boolean> getNotifications()
    {
        return new HashMap(this.notificationsOwner);
    }
}
