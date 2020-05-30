package PL.AssociationUI;

import BL.Client.ClientSystem;
import BL.Client.Handlers.AssociationManagementUnit;
import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.HashMap;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

public class TeamController extends AInitComboBoxObjects{

//    private ClientServerCommunication clientServerCommunication = new ClientServerCommunication();
//
//    private AssociationManagementUnit associationManagementUnit = new AssociationManagementUnit(clientServerCommunication);

    //create team
    @FXML
    private JFXTextField teamName;

    @FXML
    private JFXTextField teamOwnerUsername;

    //set team in league season
    @FXML
    private JFXComboBox<Season> seasons;

    @FXML
    private JFXComboBox<LeagueSeason> leagueSeasons;

    @FXML
    private JFXComboBox<Team> teams;

    public void initTeamInLeagueSeasonsComboBoxOptions(){
        if (!initSeasonChoices(seasons) || !initTeamChoices(teams)){
            closeWindow();
            return;
        }

        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                if(!initLeagueSeasonsChoices(leagueSeasons, seasons.getValue()))
                    closeWindow();
            }
        });
    }

    public void addTeam(ActionEvent actionEvent) {
        Fan fan = getFanByUsername(teamOwnerUsername.getText());

        try {
            App.clientSystem.associationManagementUnit.addTeam(teamName.getText(), teamOwnerUsername.getText(), fan);
            //showMessage(true);
            showSimpleAlert("Success", "Team was added successfully");
        } catch (IllegalArgumentException e){
            //showMessage(false);
            showSimpleAlert("Error", e.getMessage());
        }

        clearFieldsCreateTeam(actionEvent);
    }

    public void clearFieldsCreateTeam(ActionEvent mouseEvent) {
        if (teamName != null) teamName.clear();
        if (teamOwnerUsername != null) teamOwnerUsername.clear();
    }

    public void setTeamInLeagueSeason(){

        LeagueSeason leagueSeason;
        Team team;

        try {

            if (seasons.getValue() == null || leagueSeasons.getValue() == null || teams.getValue() == null){
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            leagueSeason = leagueSeasons.getValue();
            team = teams.getValue();

            if (App.clientSystem.leagueSeasonUnit.addTeamToLeagueSeason(leagueSeason,team)){
                showSimpleAlert("Success", "Team added to the league successfully!");
            } else {
                showSimpleAlert("Error", "There was a problem with the server. Please try again later");
            }
            closeWindow();

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }

    }

    public void closeWindow() {
        AssociationController.loadScreen("AssociationManageTeamsFXML");
    }

    private Fan getFanByUsername(String username) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("username", username);
        List<User> user = ClientSystem.communication.query("UserByUsername", args);

        if (user == null || user.isEmpty() || !(user.get(0) instanceof Fan)) return null;
        return (Fan) user.get(0);

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
}
