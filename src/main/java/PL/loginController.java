package PL;

import BL.Client.Handlers.HandleUserUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class loginController
{

    @FXML
    private javafx.scene.control.TextField usernameTextField;
    @FXML
    private javafx.scene.control.PasswordField passwordPasswordField;

    private HandleUserUnit handleUserUnit;

    public void initialize (HandleUserUnit handleUserUnit)
    {
        this.handleUserUnit = handleUserUnit;
    }

    public boolean loginClicked()
    {
        if(usernameTextField == null || usernameTextField.equals("") || passwordPasswordField==null || passwordPasswordField.equals(""))
        {
            Alert badInputAlert = new Alert(Alert.AlertType.ERROR);
            badInputAlert.setTitle("Bad input");
            badInputAlert.setHeaderText("Bad input");
            badInputAlert.setContentText("Username or Password are not filled in");

            badInputAlert.showAndWait();

            return false;
        }

        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        return handleUserUnit.logIn(username, password);
    }

}
