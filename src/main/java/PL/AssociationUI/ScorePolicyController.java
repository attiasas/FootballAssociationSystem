package PL.AssociationUI;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.ScorePolicy;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;


import static PL.AlertUtil.showSimpleAlert;

public class ScorePolicyController extends AInitComboBoxObjects {

    //create new score policy screen
    @FXML
    private JFXTextField winPoints;
    @FXML
    private JFXTextField drawPoints;
    @FXML
    private JFXTextField losePoints;

    //change score policy screen
    @FXML
    JFXComboBox<League> leagues;
    @FXML
    JFXComboBox<Season> seasons;
    @FXML
    JFXComboBox<ScorePolicy> scorePolicies;

    public void initComboBoxOptions(){
        initLeagueChoices(leagues);
        initScorePolicyChoices(scorePolicies);
        initSeasonChoices(seasons);
    }

    public void createScorePolicy() {
        int win, draw, lose;
        win = draw = lose = 0;

        try {
            if (winPoints.getText() != null && !winPoints.getText().equals("")
                    && drawPoints.getText() != null && !drawPoints.getText().equals("")
                    && losePoints.getText() != null && !losePoints.getText().equals("")) {
                win = Integer.parseInt(winPoints.getText());
                draw = Integer.parseInt(drawPoints.getText());
                lose = Integer.parseInt(losePoints.getText());
                App.clientSystem.policiesUnit.addNewScorePolicy(win, draw, lose);
                showSimpleAlert("Success", "Score Policy added successfully!");
            } else {
                showSimpleAlert("Error", "Please fill the required (*) fields.");
            }
        } catch (NumberFormatException e){
            showSimpleAlert("Error","Please insert only numbers.");
        } catch (Exception e){
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void changeScorePolicy() {
        League league;
        Season season;
        ScorePolicy newScorePolicy;
        LeagueSeason leagueSeason;
        try {
            if (leagues.getValue() == null || seasons.getValue() == null || scorePolicies.getValue() == null) {
                throw new Exception("Please fill all the fields.");
            }

            league = leagues.getValue();
            season = seasons.getValue();
            newScorePolicy = scorePolicies.getValue();

            leagueSeason = App.clientSystem.leagueSeasonUnit.getLeagueSeason(season,league);
            App.clientSystem.leagueSeasonUnit.changeScorePolicy(leagueSeason,newScorePolicy);
            showSimpleAlert("Success","Score Policy changed successfully!");

        } catch (Exception e){
            showSimpleAlert("Error", e.getMessage());
        }
    }



}
