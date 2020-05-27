package BL.Server;

import BL.Server.utils.Configuration;
import BL.Server.utils.DB;
import DL.Users.Notifiable;
import DL.Users.Notification;
import DL.Users.User;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Description:     X
 * ID:              X
 **/
public class NotificationUnit
{

    private Map<User, InetAddress> subscribedUsers; //holds InetAddress port that we decided
    private final int clientPort = Integer
            .parseInt(Configuration.getPropertyValue("clientNotification.port"));

    public NotificationUnit()
    {
        this.subscribedUsers = new HashMap<>();
    }

    /**
     * saves the notifications for all the users that need to get them and
     * sends notifications to all the clients that are subscribed to notifications ans shoud get the notification
     * @param notifiable
     * @return
     */
    public boolean notify(Notifiable notifiable)
    {
        Set<User> usersToNotify = notifiable.getNotifyUsersList();
        Notification notification = notifiable.getNotification();


        for(User user : usersToNotify)
        {
            //add to the server side and to the db on offline

            user.addNotification(notification);

            DB.merge(user);


            //send the notifications to the users that are subscribed
            try
            {
                if(this.subscribedUsers.containsKey(user))
                {//user is subscribed for live notifications, send the user client the notification
                    InetAddress userIP = this.subscribedUsers.get(user);
                    Socket toUserSocket = new Socket(userIP, clientPort);

                    ObjectOutputStream out = new ObjectOutputStream(toUserSocket.getOutputStream());

                    out.writeObject(notification);
                    out.flush();
                    out.close();

                    //after sending the notification to the user, mark it as read in the server
                    user.markNotificationAsRead(notification);
                    //TODO: mark the notification as read in the DB too
                    DB.merge(user);
                }
            }
            catch(Exception e)
            {

            }
        }


        return true;
    }


    /**
     * add a user to the subscribers list - so he will be able to get live notifications
     * @param user
     * @param ipAddress
     * @return
     */
    public boolean subscribeUser(User user, InetAddress ipAddress)
    {
        if(this.subscribedUsers.containsKey(user))
        {
            return false;
        }

        this.subscribedUsers.put(user, ipAddress);
        return true;
    }


    /**
     * remove a user from the live notifications subscribers list
     * @param user
     * @return
     */
    public boolean unsubscribeUser(User user)
    {
        if(!this.subscribedUsers.containsKey(user))
        {
            return false;
        }

        this.subscribedUsers.remove(user);
        return true;
    }

    public boolean markAllNotificationsOfUserAsRead(User user) {
        //TODO: mark the notifications as read in the DB
        user.markAllNotificationsAsRead();
        return DB.merge(user);
    }


}
