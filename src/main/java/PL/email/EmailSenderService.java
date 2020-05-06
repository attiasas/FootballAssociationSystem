package PL.email;

import BL.Server.utils.EmailUtil;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Unit class
 */
public class EmailSenderService {

    private static final String NOTIFY_TITLE = "Sportify Notification";
    private static String emailText;

    public static void main(String[] args) {
//        ArrayList<String> recepients = new ArrayList<>(Arrays.asList(" gabayam@post.bgu.ac.il","amitdamr@post.bgu.ac.i", "assafattias93@gmail.com", "dvirsim@post.bgu.ac.il", "avihaipp@gmail.com"));
        ArrayList<String> recepients = new ArrayList<>(Collections.singletonList("avihaipp@gmail.com"));
        ArrayList<String> content = new ArrayList<>(Arrays.asList("User", "06/05/2020", "Barcelona", "Liverpool", "6", "7$", "UEFA Champions League"));
        EmailSenderService.sentNotification(recepients, content);
    }

    /**
     * read the HTML mail template using Jsout html parser.
     */
    static void readTemplate() {
        try {
            ClassLoader classLoader = new EmailSenderService().getClass().getClassLoader();
            File mailContent = new File(classLoader.getResource("templates/mail-content.html").getFile());
            emailText = Jsoup.parse(mailContent, "UTF-8").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sent a Notification according to template e.g 7 fields (username, date, home, away, daystomatch, ticket cost, league).
     *
     * @param recipients list of addresses to get the notifification
     * @param content    - the content of the mail according to the mail template
     */
    static void sentNotification(List<String> recipients, List<String> content) {
        for (String recipient : recipients) {
            readTemplate();
            emailText = emailText.replace("{{0}}", content.get(0));
            emailText = emailText.replace("{{1}}", content.get(1));
            emailText = emailText.replace("{{2}}", content.get(2));
            emailText = emailText.replace("{{3}}", content.get(3));
            emailText = emailText.replace("{{4}}", content.get(4));
            emailText = emailText.replace("{{5}}", content.get(5));
            emailText = emailText.replace("{{6}}", content.get(6));
            EmailUtil.sendMail(recipient, emailText, NOTIFY_TITLE);
        }
    }
}
