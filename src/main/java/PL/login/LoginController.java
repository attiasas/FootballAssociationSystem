package PL.login;

import BL.Client.Handlers.HandleUserUnit;
import BL.Communication.ClientServerCommunication;
import PL.main.App;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@Log4j
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private FontAwesomeIconView fa_lock;

    @FXML
    private JFXCheckBox cb_session;

    @FXML
    private JFXSpinner loggingProgress;

    private Set<String> possibleSuggestions;

    private HandleUserUnit userUnit;

    private ClientServerCommunication clientServerCommunication;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        possibleSuggestions = new HashSet<>();
        String[] pw = {"admin", "amir", "asaf", "avihai", "dvir", "amit"};
        Collections.addAll(possibleSuggestions, pw);
        //clientServerCommunication = new ClientServerCommunication();
        fa_lock.setVisible(true);
        loggingProgress.setVisible(false);
        TextFields.bindAutoCompletion(txt_username, possibleSuggestions);
    }

    @FXML
    void completeLogin() {
        fa_lock.setVisible(true);
        loggingProgress.setVisible(false);
        String username = StringUtils.trimToEmpty(this.txt_username.getText());
        String password = StringUtils.trimToEmpty(this.txt_password.getText());
        if (App.clientSystem.userUnit.logIn(username, password)) {
            closeStage();
            loadMain();
            log.info("User successfully logged in " + username);
        } else {
            this.txt_username.getStyleClass().add("wrong-credentials");
            this.txt_password.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        fa_lock.setVisible(false);
        loggingProgress.setVisible(true);
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(4));
        pauseTransition.setOnFinished(ev -> {
            completeLogin();
        });
        pauseTransition.play();
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {

    }

    @FXML
    void handleOpenSignUpButton(ActionEvent event) {
        loadStage("Signup");
    }

    void loadMain() {
        loadStage("MainApp");
    }

    public void handleGuestButton(ActionEvent actionEvent) {
        loadMain();
    }

    private void closeStage() {
        ((Stage) txt_username.getScene().getWindow()).close();
    }

    public void loadStage(String fxmlFileName) {
        try {
            closeStage();
            Parent parent = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setTitle("Sportify");
            stage.setScene(new Scene(parent));
            App.mainStage = stage;
            stage.show();
        } catch (IOException ex) {
            log.info(ex.getMessage());
        }
    }
}
