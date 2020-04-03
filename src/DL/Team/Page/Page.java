package DL.Team.Page;

import javax.persistence.*;

/**
 * Description: Defines a Page object - a personal page of coach/player/team fan can follow    X
 * ID:              X
 **/

@MappedSuperclass
public abstract class Page
{
    @Column
    public String content;

}
