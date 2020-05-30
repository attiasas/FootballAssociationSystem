package PL.Notifications;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import DL.Users.Fan;
import DL.Users.Notifiable;
import DL.Users.Notification;
import DL.Users.User;
import PL.main.App;
import ch.qos.logback.core.net.server.Client;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import dorkbox.util.jna.windows.structs.MOUSE_EVENT_RECORD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j;
import javafx.scene.control.Label;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Log4j
public class NotificationsController implements Initializable, Serializable
{

    private User currUser;

    @FXML
    private ListView list;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.currUser = ClientSystem.getLoggedUser();
        loadAllNotifications();
    }



    private List<NotificationCell> getNotificationCellList(Map<Notification, Boolean> map)
    {
        List<NotificationCell> cellList = new ArrayList<>();
        for(Map.Entry<Notification, Boolean> entry : map.entrySet())
        {
            Notification notification = entry.getKey();
            boolean isReadNotification = entry.getValue();

            NotificationCell nCell = new NotificationCell(notification, isReadNotification);
            cellList.add(nCell);
        }

        return cellList;
    }


    private void initForTest()
    {
        // for the test
        //------------------------------------------------------------------------------------
        User admin = new Fan("admin", "admin", "admin");
        ClientSystem.logIn(admin);
        Notification notification = new Notification("A");
        admin.addNotification(notification);
        admin.markNotificationAsRead(notification);
//        try{
//            TimeUnit.SECONDS.sleep(1);
//        }
//        catch (Exception e)
//        {
//
//        }
        admin.addNotification(new Notification("B"));
//        try{
//            TimeUnit.SECONDS.sleep(1);
//        }
//        catch (Exception e)
//        {
//
//        }
        admin.addNotification(new Notification("C"));
//        try{
//            TimeUnit.SECONDS.sleep(1);
//        }
//        catch (Exception e)
//        {
//
//        }
//        admin.addNotification(new Notification("D"));

        //-------------------------------------------------------------------------------------
    }

    private void loadAllNotifications()
    {

//        initForTest();



       // Map<Notification, Boolean> map = ClientSystem.getLoggedUser().getNotifications();
        Map<Notification, Boolean> map = ClientSystem.getLoggedUser().getNotifications();

        List<NotificationCell> cellList = getNotificationCellList(map);

        ObservableList<NotificationCell> items = FXCollections.observableArrayList(cellList);
        //sort the list by notifications date
        Collections.sort(items);
        //add the notificationCell items to the ListView
        list.setItems(items);


        list.setCellFactory(param -> new ListCell<NotificationCell>(){

            ImageView imageView = new ImageView();

            @Override
            protected void updateItem(NotificationCell item, boolean empty)
            {
                super.updateItem(item, empty);

                if(empty || item == null || item.getContent() == null)
                {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                }
                else
                {
                    SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    setText(String.format(item.getContent() + "\n" + dateParser.format(item.getCreationDate())));

                    //import the image
                    File file = new File("src/main/resources/img/notification.png");
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    setGraphic(imageView);

                    setMouseTransparent(true); // set not selectable

                    //set color of notification based on if it was read
                    if(item.getIsRead() == false)
                    {
                        setStyle("-fx-background-color: #828181");
                    }
                    else
                    {
                        setStyle("-fx-background-color: #3e3e3e");
                    }
                }
            }
        });
    }



    public static class NotificationCell implements Comparable<NotificationCell>
    {
        private final String content;
        private final boolean isRead;
        private final Date creationDate;


        public NotificationCell(Notification notification, boolean isRead)
        {
            this.content = notification.getMsg();
            this.isRead = isRead;
            this.creationDate = notification.getCreationDate();
        }

        public String getContent()
        {
            return content;
        }

        public boolean getIsRead()
        {
            return isRead;
        }

        public Date getCreationDate()
        {
            return this.creationDate;
        }

        /**
         * compare two NotificationCell objects based on if they were already read and their creation date
         * @param other
         * @return
         */
        @Override
        public int compareTo(NotificationCell other)
        {
            if(this.isRead == true && other.isRead == false)
            {
                return 1;
            }
            else if(this.isRead == false && other.isRead == true)
            {
                return -1;
            }
            else
            {
                //both are either true or both are false
                if(this.creationDate.compareTo(other.creationDate) > 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    public void closeWindow()
    {
        App.mainStage.setScene(App.scenes.pop());
//        sendTestNotification();
        markLoggedUserNotificationsAsRead();

        // -------------------- Testing Notifications ---------------------

//        ClientServerCommunication.notifyTestServer();

        // ---------------------------------------------------------------
    }

    private void markLoggedUserNotificationsAsRead()
    {
        User loggedUser = ClientSystem.getLoggedUser();
        loggedUser.markAllNotificationsAsRead();
    }


}
