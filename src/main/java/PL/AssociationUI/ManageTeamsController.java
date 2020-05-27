package PL.AssociationUI;

import BL.Client.Handlers.AssociationManagementUnit;
import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationAssociationManagementStub;
import DL.Users.Fan;
import DL.Users.User;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static PL.AlertUtil.showSimpleAlert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageTeamsController extends AInitComboBoxObjects {

    private ClientServerCommunication clientServerCommunication = new ClientServerCommunication();

    private AssociationManagementUnit associationManagementUnit = new AssociationManagementUnit(clientServerCommunication);

    @FXML
    private JFXTextField teamName;

    @FXML
    private JFXTextField teamOwnerUsername;

    private Stage subStage;

    public void addTeam(ActionEvent actionEvent) {
        Fan fan = getFanByUsername(teamOwnerUsername.getText());

        try {
            associationManagementUnit.addTeam(teamName.getText(), teamOwnerUsername.getText(), fan);
            //showMessage(true);
            showSimpleAlert("Success", "Team was added successfully");
        } catch (IllegalArgumentException e){
            //showMessage(false);
            showSimpleAlert("Error", e.getMessage());
        }

        clearFieldsCreateTeam(actionEvent);
    }


    public void closeMessage(ActionEvent mouseEvent) {
        subStage.close();
    }

    public void clearFieldsCreateTeam(ActionEvent mouseEvent) {
        if (teamName != null) teamName.clear();
        if (teamOwnerUsername != null) teamOwnerUsername.clear();
    }

     /*   private void showMessage(boolean success)
        {
            try {
                subStage = new Stage();
                Parent root;
                if (success)
                {
                    subStage.setTitle("Insertion Succeeded");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    root = fxmlLoader.load(getClass().getResource("/Window/SuccessMsg.fxml").openStream());
                }
                else
                {
                    subStage.setTitle("Insertion Failed");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    root = fxmlLoader.load(getClass().getResource("/Window/FailureMsg.fxml").openStream());
                }

                Scene scene = new Scene(root, 400, 350);
                subStage.setScene(scene);
                subStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                subStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    private Fan getFanByUsername(String username) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("username", username);
        List<User> user = clientServerCommunication.query("UserByUsername", args);

        if (user == null || user.isEmpty() || !(user.get(0) instanceof Fan)) return null;
        return (Fan) user.get(0);

    }

}
