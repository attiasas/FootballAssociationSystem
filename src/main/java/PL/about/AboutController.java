package PL.about;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    private static final String LINKED_IN = "https://www.linkedin.com/";
    private static final String FACEBOOK = "http://facebook.com/";
    private static final String WEBSITE = "https://in.bgu.ac.il/Pages/default.aspx";
    private static final String GITHUB = "https://github.com/attiasas/FootballAssociationSystem";

    public static void loadWebpage(String url) {
        String OS = SystemUtils.OS_NAME;
        /* Windows OS */
        if (OS.startsWith("Windows")) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE))
                    desktop.browse(new URL(url).toURI());
            } catch (IOException | URISyntaxException e1) {
                System.out.println("fail to load page");
                e1.printStackTrace();
            }
        } else { /* UNIX platform - Ubuntu OS */
            try {
                new ProcessBuilder("x-www-browser", url).start();
            } catch (IOException e) {
                System.out.println("fail to load page");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void loadGithub() {
        loadWebpage(GITHUB);
    }

    @FXML
    private void loadBlog() {
        loadWebpage(WEBSITE);
    }

    @FXML
    private void loadLinkedIN() {
        loadWebpage(LINKED_IN);
    }

    @FXML
    private void loadFacebook() {
        loadWebpage(FACEBOOK);
    }
}