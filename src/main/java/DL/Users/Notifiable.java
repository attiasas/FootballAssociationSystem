package DL.Users;

import java.util.Set;

public interface Notifiable
{
    Notification getNotification();

    Set getNotifyUsersList();
}
