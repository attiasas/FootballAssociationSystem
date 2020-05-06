package BL.Server.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Serfati
 */
@Log4j(topic = "event")
public class EmailUtil {


    /**
     * @param recipient the email address of the receiver
     */
    public static void sendTestMail(String recipient) {
        Runnable emailSendTask = () -> {
            final String logged = String.format("Initiating email sending task. Sending to %s", recipient);
            log.log(Level.INFO, logged);
            Properties props = new Properties();
            try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                props.put("mail.imap.ssl.trust", "*");
                props.put("mail.imap.ssl.socketFactory", sf);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", Boolean.parseBoolean(Configuration.getPropertyValue("mail.smtp.starttls.enable")) ? "true" : "false");
                props.put("mail.smtp.host", Configuration.getPropertyValue("mail.smtp.host"));
                props.put("mail.smtp.port", Integer.parseInt(Configuration.getPropertyValue("mail.smtp.port")));

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Configuration.getPropertyValue("mailserver.email"),
                                Configuration.getPropertyValue("mailserver.password"));
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(Configuration.getPropertyValue("mailserver.email")));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipient));
                message.setSubject("Test mail from Sportify");
                message.setText("Hi,"
                        + "\n\n This is a test mail from Sportify!");
                Transport.send(message);
                log.info("Everything seems fine");
            } catch (Throwable exp) {
                log.info("Error occurred during sending email", exp);
            }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }

    /**
     * Main class method to send a mail to a {@code recipient}.
     * Uses {@code Configuration} class which controls the  general and constants settings for mail server
     *
     * @param recipient the email address of the receiver
     * @param content   the content of the mail
     * @param title     the title of the mail
     */
    public static void sendMail(String recipient, String content, String title) {
        Runnable emailSendTask = () -> {
            final String logged = String.format("Initiating email sending task. Sending to %s", recipient);
            log.info(logged);
            Properties props = new Properties();
            try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                props.put("mail.imap.ssl.trust", "*");
                props.put("mail.imap.ssl.socketFactory", sf);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", Boolean.parseBoolean(Configuration.getPropertyValue("mail.smtp.starttls.enable")) ? "true" : "false");
                props.put("mail.smtp.host", Configuration.getPropertyValue("mail.smtp.host"));
                props.put("mail.smtp.port", Integer.parseInt(Configuration.getPropertyValue("mail.smtp.port")));

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Configuration.getPropertyValue("mailserver.email"),
                                Configuration.getPropertyValue("mailserver.password"));
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(Configuration.getPropertyValue("mailserver.email")));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(title);
                message.setContent(content, "text/html");
                Transport.send(message);
                log.info("Everything seems fine");
            } catch (Throwable exp) {
                log.info("Error occurred during sending email", exp);
            }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }
}

