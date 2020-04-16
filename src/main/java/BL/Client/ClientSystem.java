package BL.Client;

import BL.Client.Handlers.ComplaintUnit;
import BL.Client.Handlers.HandleUserUnit;
import DL.Game.Referee;
import DL.Users.User;
import DL.Users.UserComplaint;

import java.util.List;

/**
 * Description:     X ID:              X
 **/
@Setter
@Log4j
public class ClientSystem {

  private InetAddress serverIP;
  private int serverPort;
  private IClientStrategy clientStrategy;

  public ClientSystem(InetAddress IP, int port, IClientStrategy clientStrategy) {
    this.serverIP = IP;
    this.serverPort = port;
    this.clientStrategy = clientStrategy;
  }


    private static User loggedUser = null;


    public static User getLoggedUser()
    {
        return loggedUser;
    }

    public static boolean logIn(User user)
    {
        if(user == null)
        {
            return false;
        }

        loggedUser = user;
        return true;
    }


//    /**
//     *
//     * @param userName
//     * @param password
//     * @return true if user managed to log in
//     */
//    public boolean logIn (String userName, String password)
//    {
//        this.loggedUser = userUnit.logIn(userName, password);
//
//        if(this.loggedUser == null)
//        {
//            return false;
//        }
//
//        return true;
//    }
//
//
//
//    /**
//     * Signs up a user up and logging him in. This function signs a user up as a  Fan
//     * @param userName
//     * @param email
//     * @param password
//     * @return true if user was able to sign up and log in
//     */
//    public boolean signUp(String userName, String email, String password)
//    {
//        User user = userUnit.signUp(userName, email, password);
//
//        if(user == null)
//        {
//            return false;
//        }
//
//        this.loggedUser = user;
//        return true;
//    }
//
//
//    /**
//     * Signs up a new referee to the system and logging the new user that was created in
//     * @param userName
//     * @param email
//     * @param password
//     * @param name
//     * @param qualification
//     * @return
//     */
//    public boolean signUpReferee(String userName, String email, String password, String name, String qualification)
//    {
//        Referee referee = userUnit.addNewReferee(userName, email, password, name, qualification);
//        if(referee == null)
//        {
//            return false;
//        }
//
//        this.loggedUser = referee.getFan();
//        return true;
//    }
//
//
//    /**
//     * removes a user. A user can remove only himself
//     * @return
//     */
//    public boolean removeUser()
//    {
//        return userUnit.removeUser(this.loggedUser);
//    }
//
//
//
//
//
//    /**
//     * gets the complaints of a user
//     * @param user
//     * @return
//     */
//    public List<UserComplaint> showUserComplaints(User user)
//    {
//        return complaintUnit.showUserComplaints(user);
//    }
//
//
//    /**
//     * the logged user creates a complaint
//     * @param msg
//     * @return
//     */
//    public boolean createComplaint (String msg)
//    {
//        return complaintUnit.createComplaint(this.loggedUser, msg);
//    }
//
//    /**
//     *
//     * @param userComplaint
//     * @param comment
//     * @return
//     */
//    public boolean makeComment(UserComplaint userComplaint, String comment)
//    {
//        return makeComment(userComplaint, comment);
//    }


}
