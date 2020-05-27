package PL.AssociationUI;

import PL.main.App;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import static PL.AlertUtil.showSimpleAlert;


public class GamePolicyController {

    @FXML
    private JFXTextField gamesPerDay;
    @FXML
    private JFXTextField numberOfRounds;

    /**
     * Creates games policy according to the user input
     */
    public void createGamePolicy() {
        int rounds, games;
        rounds = games = 0;
        try {

            if (numberOfRounds.getText() != null && !numberOfRounds.getText().equals("") &&
                    gamesPerDay.getText() != null && !gamesPerDay.getText().equals("")) {
                rounds = Integer.parseInt(numberOfRounds.getText());
                games = Integer.parseInt(gamesPerDay.getText());
                System.out.println("Rounds: " + rounds + " ,games: " + games);

                if (App.clientSystem.policiesUnit.addNewGamePolicy(rounds, games)) {
                    showSimpleAlert("Success", "Game Policy added successfully!");
                } else {
                    showSimpleAlert("Error", "There was a problem with the server. Please try again later");
                }

            } else {
                showSimpleAlert("Error", "Please fill the required (*) fields.");
            }
            closeWindow();
        } catch (NumberFormatException n) {
            showSimpleAlert("Error", "Please insert only numbers.");
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }

    }

    public void closeWindow() {
        AssociationController.loadScreen("AssociationManagePoliciesFXML");
    }
}
