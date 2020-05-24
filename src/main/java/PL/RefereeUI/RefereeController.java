package PL.RefereeUI;

import BL.Client.ClientSystem;
import BL.Client.Handlers.MatchEventUnit;
import DL.Game.Match;
import PL.RefereeUI.EventLogController;
import PL.main.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

/**
 * Referee Main Matches GUI
 */
public class RefereeController
{
    @FXML
    public TableView<MatchView> tv_matches;
    @FXML
    public ScrollPane sp_holder;
    @FXML
    public Text t_noMatch;

    private MatchEventUnit unit;

    /**
     * Class that represents a view in the list of matches
     */
    public class MatchView
    {
        private final SimpleStringProperty homeTeamName;
        private final SimpleStringProperty awayTeamName;
        private final SimpleStringProperty matchDate;
        private final Button editButton;

        public MatchView(Match match)
        {
            homeTeamName = new SimpleStringProperty(match.getHomeTeam().getName());
            awayTeamName = new SimpleStringProperty(match.getAwayTeam().getName());
            matchDate = new SimpleStringProperty(match.getStartTime().toString());

            editButton = new Button("Edit");
            editButton.onActionProperty().set(event -> {
                try {
                    editMatchEvents(match);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        public Button getEditButton() {
            return editButton;
        }

        public String getHomeTeamName() {
            return homeTeamName.get();
        }

        public SimpleStringProperty homeTeamNameProperty() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName.set(homeTeamName);
        }

        public String getAwayTeamName() {
            return awayTeamName.get();
        }

        public SimpleStringProperty awayTeamNameProperty() {
            return awayTeamName;
        }

        public void setAwayTeamName(String awayTeamName) {
            this.awayTeamName.set(awayTeamName);
        }

        public String getMatchDate() {
            return matchDate.get();
        }

        public SimpleStringProperty matchDateProperty() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate.set(matchDate);
        }
    }

    /**
     * Init GUI
     * @param unit
     */
    public void init(MatchEventUnit unit)
    {
        // check if logged user is referee and load matches
        this.unit = unit;
        List<Match> matches = unit.getActiveMatches(ClientSystem.getLoggedUser());
        if(matches == null)
            throw new IllegalArgumentException("User Must be a Referee / Bad Communication");
        else if(matches.isEmpty())
        {
            t_noMatch.setVisible(true);
            sp_holder.setVisible(false);
        }
        else
        {
            // prepare table content
            t_noMatch.setVisible(false);
            sp_holder.setVisible(true);

            List<MatchView> viewList = new ArrayList<>();
            for(int i = 0; i < matches.size(); i++) viewList.add(new MatchView(matches.get(i)));

            tv_matches.prefHeightProperty().bind(App.mainStage.heightProperty());
            tv_matches.prefWidthProperty().bind(App.mainStage.widthProperty());

            // set table and add content
            TableColumn tc_homeTeam = new TableColumn("Home Team");
            tc_homeTeam.setCellValueFactory(new PropertyValueFactory<MatchView,String>("homeTeamName"));
            //tc_homeTeam.prefWidthProperty().setValue(250);
            tv_matches.getColumns().add(tc_homeTeam);

            TableColumn tc_awayTeam = new TableColumn("Away Team");
            tc_awayTeam.setCellValueFactory(new PropertyValueFactory<MatchView,String>("awayTeamName"));
            //tc_awayTeam.prefWidthProperty().setValue(250);
            tv_matches.getColumns().add(tc_awayTeam);

            TableColumn tc_matchDate = new TableColumn("Match Date");
            tc_matchDate.setCellValueFactory(new PropertyValueFactory<MatchView,String>("matchDate"));
            tv_matches.getColumns().add(tc_matchDate);

            TableColumn tc_action = new TableColumn("Action");
            tc_action.setCellValueFactory(new PropertyValueFactory<MatchView,Button>("editButton"));
            tv_matches.getColumns().add(tc_action);

            tv_matches.setItems(FXCollections.observableList(viewList));
        }
    }

    /**
     * method for editing event log of a given match
     * @param match - to change his event log
     * @throws IOException
     */
    public void editMatchEvents(Match match) throws IOException
    {
        EventLogController controller = (EventLogController) App.loadScreen("matchEventsScreen");
        controller.initialize(unit,match);
    }

    /**
     * Return to the previous scene
     */
    public void closeWindow() {
        App.mainStage.setScene(App.scenes.pop());
    }
}
