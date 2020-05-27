package PL.signup;

import BL.Client.Handlers.AssociationManagementUnit;
import BL.Client.Handlers.HandleUserUnit;
import BL.Client.Handlers.NomineePermissionUnit;
import BL.Client.Handlers.TeamAssetUnit;
import BL.Communication.ClientServerCommunication;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

@Log4j
public class SignupController implements Initializable {

    @FXML
    public StackPane parentContainer;

    public AnchorPane anchorPane;

    @FXML
    private JFXTextField txt_fullName;

    @FXML
    private JFXTextField txt_email;

    @FXML
    private JFXTextField txt_confirmMail;

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXDatePicker txt_dob;

    @FXML
    private JFXRadioButton rb_male;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private JFXRadioButton rb_female;

    @FXML
    private JFXRadioButton rb_nonbinary;

    @FXML
    private JFXCheckBox cb_terms;

    private HandleUserUnit userUnit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb_male.setToggleGroup(toggleGroup);
        rb_female.setToggleGroup(toggleGroup);
        rb_nonbinary.setToggleGroup(toggleGroup);
        userUnit = new HandleUserUnit(new ClientServerCommunication(),
                new NomineePermissionUnit(new ClientServerCommunication()),
                new TeamAssetUnit(new ClientServerCommunication()),
                new AssociationManagementUnit(new ClientServerCommunication()));
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        loadStage("Login");
    }

    @FXML
    void handleSignupButton(ActionEvent event) {
        if (validation()) loadMainApp();
        //FOR TESTING UNCOMMENT
        //  loadMainApp();
    }

    private boolean validation() {

        String fullName = StringUtils.trimToEmpty(this.txt_fullName.getText());
        String username = StringUtils.trimToEmpty(this.txt_username.getText());
        String password = StringUtils.trimToEmpty(this.txt_password.getText());
        String email = StringUtils.trimToEmpty(this.txt_email.getText());
        String cemail = StringUtils.trimToEmpty(this.txt_confirmMail.getText());
        LocalDate dob = txt_dob.getValue();

        if (fullName.length() < 4) {
            this.txt_fullName.getStyleClass().add("wrong-credentials");
        } else
            this.txt_fullName.getStyleClass().add("correct-credentials");

        if (username.length() < 4) {
            this.txt_username.getStyleClass().add("wrong-credentials");
        } else
            this.txt_username.getStyleClass().add("correct-credentials");

        if (password.length() < 4) {
            this.txt_password.getStyleClass().add("wrong-credentials");
        } else
            this.txt_password.getStyleClass().add("correct-credentials");

        if (!cb_terms.isSelected()) {
            this.cb_terms.getStyleClass().add("wrong-credentials");
//            AlertUtil.showSimpleAlert("Sportify", "Please accept terms of use.");
        } else
            this.cb_terms.getStyleClass().add("correct-credentials");

        if (!email.equals(cemail) || email.length() < 4) {
            this.txt_email.getStyleClass().add("wrong-credentials");
            this.txt_confirmMail.getStyleClass().add("wrong-credentials");
            return false;
        } else {
            this.txt_email.getStyleClass().add("correct-credentials");
            this.txt_confirmMail.getStyleClass().add("correct-credentials");
        }


        if (dob == null) {
            this.txt_dob.getStyleClass().add("wrong-credentials");
            return false;
        }

        Instant instant = Instant.from(dob.atStartOfDay(ZoneId.systemDefault()));
        Date dateOfBirth = Date.from(instant);

        if (userUnit.signUp(username, email, password) != null) {
            log.info("User successfully sign in " + username);
        } else {
            this.txt_username.getStyleClass().add("wrong-credentials");
            return false;
        }
        return true;
    }

    @FXML
    private void loadMainApp() {
        //TODO fade in
        loadStage("MainApp");
    }

    private void closeStage() {
        ((Stage) txt_username.getScene().getWindow()).close();
    }

    public void loadStage(String fxmlFileName) {
        try {
            closeStage();
            Parent parent = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Sportify");

            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            log.info(ex.getMessage());
        }
    }
}
