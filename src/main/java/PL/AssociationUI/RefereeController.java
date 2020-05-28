package PL.AssociationUI;

import BL.Client.ClientSystem;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Referee;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import BL.Client.Handlers.AssociationManagementUnit;
import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationAssociationManagementStub;
import DL.Game.Referee;
import DL.Users.Fan;
import DL.Users.User;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

public class RefereeController extends AInitComboBoxObjects {

    @FXML
    private JFXComboBox<Season> seasons;

    @FXML
    private JFXComboBox<LeagueSeason> leagueSeasons;

    @FXML
    private JFXComboBox<Referee> referees;

    @FXML
    public ComboBox<String> comboboxReferee;

    @FXML
    private JFXTextField refereeUsername;

    @FXML
    private JFXTextField refereeName;

    @FXML
    private JFXTextField refereeQualification;

    private ObservableList<String> referees_username_FX;

    public void initRefereesInLeagueComboBoxOptions() {
        if (!initSeasonChoices(seasons) || !initRefereesChoices(referees)) {
            closeWindow();
            return;
        }

        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                if (!initLeagueSeasonsChoices(leagueSeasons, seasons.getValue()))
                    closeWindow();
            }
        });

    }

    public void setRefereesInLeagueSeason() {
        LeagueSeason leagueSeason;
        Referee referee;

        try {
            if (seasons.getValue() == null || leagueSeasons.getValue() == null || referees.getValue() == null) {
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            leagueSeason = leagueSeasons.getValue();
            referee = referees.getValue();

            if (App.clientSystem.leagueSeasonUnit.setRefereeInLeagueSeason(leagueSeason, referee)) {
                showSimpleAlert("Success", "Referee added successfully to the required LeagueSeason!");
            } else {
                showSimpleAlert("Error", "There was a problem with the server. Please try again later");
            }
            closeWindow();
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void closeWindow() {
        App.loadScreen("AssociationManageRefereesFXML");
    }

    public void initCreateRefereeComboBoxOptions() {

        referees_username_FX = FXCollections.observableArrayList();
        comboboxReferee.setItems(referees_username_FX);
        List<Referee> referees = App.clientSystem.associationManagementUnit.loadAllReferees();

        if (referees == null) return;

        List<String> referees_username = new ArrayList<>();
        for (Referee referee : referees)
            referees_username.add(referee.getFan().getUsername());
        referees_username_FX.addAll(referees_username);

    }


    public void addReferee(ActionEvent actionEvent)
    {
        Fan fan = App.clientSystem.userUnit.loadFanByUsername(refereeUsername.getText());
        try
        {
            App.clientSystem.associationManagementUnit.addNewReferee(fan, refereeName.getText(), refereeQualification.getText());
            referees_username_FX.add(refereeUsername.getText());
            showSimpleAlert("Success","Referee was added successfully");
        }

        catch (IllegalArgumentException e)
        {
            showSimpleAlert("Error",e.getMessage());
        }

        clearFieldsCreateReferee(actionEvent);

    }

    public void removeReferee(ActionEvent actionEvent)
    {
        Fan fan = App.clientSystem.userUnit.loadFanByUsername(comboboxReferee.getValue());
        if (fan == null) {
            showSimpleAlert("Error","Please fill the required (*) fields.");
        }

        Referee referee = App.clientSystem.associationManagementUnit.loadReferee(fan);

        if (App.clientSystem.associationManagementUnit.removeReferee(referee))
        {
            referees_username_FX.remove(comboboxReferee.getValue());
            showSimpleAlert("Success","Referee was removed successfully");

        }

        else
        {
            showSimpleAlert("Error","Please fill the required (*) fields.");
        }

    }

    public void clearFieldsCreateReferee(ActionEvent mouseEvent)
    {
        if (refereeName != null) refereeName.clear();
        if (refereeUsername != null) refereeUsername.clear();
        if (refereeQualification != null) refereeQualification.clear();
    }

}
