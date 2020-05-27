package PL.main;

import BL.Client.ClientSystem;
import DL.Users.Fan;
import DL.Users.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static PL.AlertUtil.showSimpleAlert;

@Log4j(topic = "event")
public class App extends Application {

    public static Stage mainStage;
    public static Stack<Scene> scenes;
    public static ClientSystem clientSystem = new ClientSystem();

    /**
     * The main function that runs the entire program
     *
     * @param args - ignored
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        log.info("Sportify launched");


        //------------------- Testing Notifications --------------------

//        User userAdmin = new Fan("admin", "admin", "admin");
//        ClientSystem.logIn(userAdmin);

        // -------------------------------------------------------------

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
//        AlertUtil.showSimpleAlert("Sportify Alert","some alert content");
//        AlertUtil.showErrorMessage("IOException","some exception contenet");
//        AlertUtil.showErrorMessage(new IOException(),"IOException","some exception contenet");
//        AlertUtil.showErrorMessage(new IOException());
        primaryStage.setResizable(false);

        mainStage = primaryStage;
        scenes = new Stack<>();
        clientSystem = new ClientSystem();

        primaryStage.show();
        new Thread(() -> {
        }).start();
    }

    public static void loadStage(String fxmlFileName) {
        try {
            Parent parent = FXMLLoader.load(App.class.getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Sportify");
            stage.setScene(new Scene(parent));
            mainStage = stage;
            stage.show();
        } catch (IOException ignored) {
        }
    }

    public static Object loadScreen(String fxmlFileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File(String.format("src/main/resources/Window/%s.fxml", fxmlFileName)).toURI().toURL());

            Scene scene = new Scene(fxmlLoader.load());
            Object controller = fxmlLoader.getController();

            scenes.push(mainStage.getScene());
            mainStage.setScene(scene);

            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            showSimpleAlert("Error", "Can't load screen. Please try again");
            return null;
        }
    }

    public static void back()
    {
        if(scenes.size() > 1)
        {
            App.mainStage.setScene(App.scenes.pop());
        }
        else mainStage.close();
    }
}
