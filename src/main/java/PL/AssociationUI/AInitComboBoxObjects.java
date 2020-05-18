package PL.AssociationUI;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import PL.main.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

public abstract class AInitComboBoxObjects {

    public void initLeagueChoices(ComboBox<League> leagueNames) {
        try {
            ObservableList<League> leaguesList = FXCollections.observableArrayList();
            leagueNames.setItems(leaguesList);
            List<League> leagues = new ArrayList<>();
            leagues.add(new League("check"));
            leagues.add(new League("chdck2"));
            leagues.add(new League("chdck3"));

            /**TODO: REMOVE THE COMMENT WHEN COMMUNICATION IS WORKING*/
            //List<League> leagues = leagueSeasonUnit.getLeagues();
            if (leagues != null && leagues.size() > 0) {
                leaguesList.addAll(leagues);
                leagueNames.setPromptText("Please select league");
                leagueNames.setTooltip(new Tooltip("Select the league"));
                //FxUtilComboBoxAutoComplete.autoCompleteComboBoxPlus(leagueNames, (typedText, itemToCompare) -> itemToCompare.getName().toLowerCase().contains(typedText.toLowerCase()));

            } else {
                closeWindow();
                showSimpleAlert("Error", "Please create a new League First and then try again.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", "There was a problem with the server. please try again");
        }
    }

    public void initGamePolicyChoices(ComboBox<GamePolicy> gamePolicies) {
        try {
            ObservableList<GamePolicy> gamePolicyList = FXCollections.observableArrayList();
            gamePolicies.setItems(gamePolicyList);
            List<GamePolicy> gpList = new ArrayList<>();
            gpList.add(new GamePolicy(3, 4));
            gpList.add(new GamePolicy(2, 1));

            /**TODO: REMOVE THE COMMENT WHEN COMMUNICATION IS WORKING*/
            //List<GamePolicy> gpList = policiesUnit.getGamePolicies();
            if (gpList != null && gpList.size() > 0) {
                gamePolicyList.addAll(gpList);
                gamePolicies.setPromptText("Please select game policy");
                gamePolicies.setTooltip(new Tooltip("Select the game policy."));
                //FxUtilComboBoxAutoComplete.autoCompleteComboBoxPlus(gamePolicies, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()));

            } else {
                closeWindow();
                showSimpleAlert("Error", "Please create a new Game Policy First and then try again.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", "There was a problem with the server. please try again");
        }
    }

    public void initScorePolicyChoices(ComboBox<ScorePolicy> scorePolicies) {
        try {
            ObservableList<ScorePolicy> scorePolicyList = FXCollections.observableArrayList();
            scorePolicies.setItems(scorePolicyList);
            List<ScorePolicy> spList = new ArrayList<>();
            spList.add(new ScorePolicy(3, 1, 0));
            spList.add(new ScorePolicy(2, 1, 0));

            /**TODO: REMOVE THE COMMENT WHEN COMMUNICATION IS WORKING*/
            //List<ScorePolicy> spList = policiesUnit.getScorePolicies();
            if (spList != null && spList.size() > 0) {
                scorePolicyList.addAll(spList);
                scorePolicies.setPromptText("Please select score policy");
                scorePolicies.setTooltip(new Tooltip("Select the score policy."));
                //FxUtilComboBoxAutoComplete.autoCompleteComboBoxPlus(scorePolicies, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()));

            } else {
                closeWindow();
                showSimpleAlert("Error", "Please create a new Score Policy First and then try again.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", "There was a problem with the server. please try again");
        }
    }

    public void initSeasonChoices(ComboBox<Season> seasons) {
        try {
            ObservableList<Season> seasonsList = FXCollections.observableArrayList();
            seasons.setItems(seasonsList);
            /**TODO:example - should remove*/
            List<Season> addSeasonsList = new ArrayList<>();
            addSeasonsList.add(new Season(1950));
            addSeasonsList.add(new Season(2011));

            /**TODO: REMOVE THE COMMENT WHEN COMMUNICATION IS WORKING*/
            //List<Season> addSeasonsList = AssociationController.leagueSeasonUnit.getSeasons();
            if (addSeasonsList != null && addSeasonsList.size() > 0) {
                seasonsList.addAll(addSeasonsList);
                seasons.setPromptText("Please select season");
                seasons.setTooltip(new Tooltip("Select the season."));

            } else {
                closeWindow();
                showSimpleAlert("Error", "Please create a new League Season First and then try again.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", "There was a problem with the server. please try again");
        }
    }

    public void initLeagueSeasonsChoices(ComboBox<LeagueSeason> leagueSeason,Season season) {
        try {
            ObservableList<LeagueSeason> leagueSeasonsList = FXCollections.observableArrayList();
            leagueSeason.setItems(leagueSeasonsList);
            /**TODO:example - should remove*/
            List<LeagueSeason> addLeagueSeasonsList = new ArrayList<>();
            addLeagueSeasonsList.add(new LeagueSeason(new League("check"),new Season(2011),null,null,null));
            addLeagueSeasonsList.add(new LeagueSeason(new League("check1"),new Season(2011),null,null,null));
            addLeagueSeasonsList.add(new LeagueSeason(new League("check2"),new Season(2012),null,null,null));

            /**TODO: REMOVE THE COMMENT WHEN COMMUNICATION IS WORKING*/
            //List<LeagueSeason> addLeagueSeasonsList = AssociationController.leagueSeasonUnit.getLeagueSeasons(season);
            if (addLeagueSeasonsList != null && addLeagueSeasonsList.size() > 0) {
                leagueSeasonsList.addAll(addLeagueSeasonsList);
                leagueSeason.setPromptText("Please select league season");
                leagueSeason.setTooltip(new Tooltip("Select the league season."));

            } else {
                closeWindow();
                showSimpleAlert("Error", "Please create a new League Season First and then try again.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", "There was a problem with the server. please try again");
        }
    }
    
    public void closeWindow(){
        if (!App.scenes.empty())
            App.mainStage.setScene(App.scenes.pop());
    }

}
