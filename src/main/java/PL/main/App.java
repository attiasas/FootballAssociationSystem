package PL.main;

import BL.Client.ClientSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

@Log4j(topic = "event")
public class App extends Application {
    public final static Logger elog = LogManager.getLogger("error");
    public static Stage mainStage;
    public static Stack<Scene> scenes;
    public ClientSystem clientSystem;

    /**
     * The main function that runs the entire program
     *
     * @param args - ignored
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        log.info("Sportify launched");
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
        });
        long exitTime = System.currentTimeMillis();
        long milliseconds = exitTime - startTime;
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        log.info("Sportify is closing. Used for " + min + " min and " + sec + " sec.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Window/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sportify");
        primaryStage.setResizable(false);

        mainStage = primaryStage;
        scenes = new Stack<>();

        primaryStage.show();
        new Thread(() -> {
        }).start();
    }
}
