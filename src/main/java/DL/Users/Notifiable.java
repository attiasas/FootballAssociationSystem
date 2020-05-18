package DL.Users;

import java.util.List;
import java.util.Set;

public interface Notifiable
{
    /**
     *
     * @return notification object with the message
     */
    Notification getNotification();

    /**
     *
     * @return list of users to notify
     */
    Set getNotifyUsersList();

}
