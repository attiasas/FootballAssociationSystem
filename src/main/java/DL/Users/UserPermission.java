package DL.Users;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     Represents permissions of a user
 * ID:              X
 **/


@Entity
@NamedQueries( value = {
        @NamedQuery(name = "UserPermission", query = "SELECT up From UserPermission up WHERE up.id = :id")
})
public class UserPermission implements Serializable
{
    public enum Permission
    {
        ADD,REMOVE,EDIT
    }

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Permission> teamPermissions;


    public UserPermission()
    {
        this(new ArrayList<Permission>());
    }

    public UserPermission(List<Permission> permissionList)
    {
        this.teamPermissions = new ArrayList<Permission>(permissionList);
    }

    public boolean hasPermission(Permission permission)
    {
        if(teamPermissions.contains(permission))
        {
            return true;
        }
        return false;
    }

    public boolean addPermission(Permission permission)
    {
        return this.teamPermissions.add(permission);
    }

    public boolean removePermission(Permission permission)
    {
        return this.teamPermissions.remove(permission);
    }


    @Override
    public String toString() {
        return "teamPermissions=" + teamPermissions +
                '}';
    }
}