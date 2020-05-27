package BL.Server;

import BL.Server.utils.Configuration;
import DL.Users.Notifiable;
import DL.Users.Notification;
import DL.Users.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

/**
 * Description:     X
 * ID:              X
 **/
public class NotificationUnit
{

    private Map<String, InetAddress> subscribedUsers; //holds InetAddress port that we decided
    private final int clientPort = Integer
            .parseInt(Configuration.getPropertyValue("clientNotification.port"));

    private Map<String, Notifiable> updateNotifiablesMap; //maps update queries to their notifiable object

    public NotificationUnit()
    {
        this.subscribedUsers = new HashMap<>();
        this.updateNotifiablesMap = new HashMap<>();
//        initUpdateNotifiablesmap();
    }



    /**
     * add all notifiables mapped with their named queries to the map
     */
    private void initUpdateNotifiablesmap()
    {
//
//        this.updateNotifiablesMap.put("SetTeamActivity", new Notifiable() {
//
//            public Notification getNotification(Object obj)
//            {
//                if (active)
//                    return new Notification(String.format("Team: %s is open. \n You will get notifications rela it from now on.", team.getName()));
//
//                return new Notification(String.format("Team: %s was closed temporarily. \n You will no longer get notifications about it.", team.getName()));
//            }
//
//            public Set<User> getNotifyUsersList(Object obj) {
//                return team.getPage().getFollowers();
//            }
//        });


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

            //TODO: add the notification to the DB

            //send the notifications to the users that are subscribed
            try
            {
                if(this.subscribedUsers.containsKey(user.getUsername()))
                {//user is subscribed for live notifications, send the user client the notification
                    InetAddress userIP = this.subscribedUsers.get(user.getUsername());
                    Socket toUserSocket = new Socket(userIP, clientPort);

                    ObjectOutputStream out = new ObjectOutputStream(toUserSocket.getOutputStream());

                    out.writeObject(notification);
                    out.flush();
                    out.close();

                    //after sending the notification to the user, mark it as read in the server
                    user.markNotificationAsRead(notification);
                    //TODO: mark the notification as read in the DB too
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }


        return true;
    }


    /**
     * add a user to the subscribers list - so he will be able to get live notifications
     * @param username
     * @param ipAddress
     * @return
     */
    public boolean subscribeUser(String username, InetAddress ipAddress)
    {
        if(this.subscribedUsers.containsKey(username))
        {
            return false;
        }

        this.subscribedUsers.put(username, ipAddress);
        return true;
    }


    /**
     * remove a user from the live notifications subscribers list
     * @param username
     * @return
     */
    public boolean unsubscribeUser(String username)
    {
        if(!this.subscribedUsers.containsKey(username))
        {
            return false;
        }

        this.subscribedUsers.remove(username);
        return true;
    }

    public boolean markAllNotificationsOfUserAsRead(User user)
    {
        return user.markAllNotificationsAsRead();

        //TODO: mark the notifications as read in the DB
    }

}