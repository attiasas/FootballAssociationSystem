package PL.RefereeUI;

import BL.Client.ClientSystem;
import BL.Client.Handlers.MatchEventUnit;
import DL.Game.Match;
import DL.Team.Members.Player;
import DL.Team.Team;
import PL.RefereeUI.EventLogController;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

/**
 * Created By: Assaf, On 04/05/2020
 * Description:
 */
public class AddEventController
{
    @FXML
    public ComboBox cb_eventType,cb_player1,cb_player2;
    @FXML
    public GridPane gridPane;
    @FXML
    public Label l_mt,l_player1,l_player2,l_mt_add;
    @FXML
    public TextField tf_mt,tf_mt_add;

    private MatchEventUnit unit;
    private Match match;
    private EventLogController eventLogController;

    public enum EventType
    {
        YellowCard { @Override public String toString() { return DL.Game.MatchEvents.YellowCard.Type();}},
        RedCard { @Override public String toString() { return DL.Game.MatchEvents.RedCard.Type();}},
        Foul { @Override public String toString() { return DL.Game.MatchEvents.Foul.Type();}},
        Goal { @Override public String toString() { return DL.Game.MatchEvents.Goal.Type();}},
        Injury { @Override public String toString() { return DL.Game.MatchEvents.Injury.Type();}},
        Offside { @Override public String toString() { return DL.Game.MatchEvents.Offside.Type();}},
        PenaltyKick { @Override public String toString() { return DL.Game.MatchEvents.PenaltyKick.Type();}},
        PlayerChange { @Override public String toString() { return DL.Game.MatchEvents.PlayerChange.Type();}},
        TimeStop { @Override public String toString() { return DL.Game.MatchEvents.StoppageTime.Type();}},
        EndGame { @Override public String toString() { return DL.Game.MatchEvents.EndGame.Type();}}
    }

    public void initialize(MatchEventUnit unit, Match match,EventLogController eventLogController)
    {
        this.eventLogController = eventLogController;
        this.unit = unit;
        this.match = match;

        // init inputs
        l_mt = new Label("Match Time (min):");
        l_mt.fontProperty().set(new Font("system",20));
        l_player1 = new Label();
        l_player1.fontProperty().set(new Font("system",20));
        l_player2 = new Label();
        l_player2.fontProperty().set(new Font("system",20));
        l_mt_add = new Label("Time To Add (min):");
        l_mt_add.fontProperty().set(new Font("system",20));

        tf_mt = new JFXTextField();
        cb_player1 = getPlayersCB();
        cb_player2 = getPlayersCB();
        tf_mt_add = new JFXTextField();

        // init type input
        List<EventType> types = new ArrayList<>();
        for(EventType type : EventType.values()) types.add(type);

        cb_eventType.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateInputs((EventType)newValue);
        });
        cb_eventType.setItems(FXCollections.observableList(types));
    }

    public void updateInputs(EventType type)
    {
        gridPane.getChildren().clear();

        // add match time input
        gridPane.add(l_mt,0,0);
        gridPane.add(tf_mt,1,0);

        if(type.equals(EventType.YellowCard) || type.equals(EventType.RedCard) || type.equals(EventType.Goal) || type.equals(EventType.Injury) || type.equals(EventType.Offside))
        {
            // one player event
            l_player1.setText("Player:");
            gridPane.add(l_player1,0,1);
            gridPane.add(cb_player1,1,1);
        }
        else if(type.equals(EventType.TimeStop))
        {
            gridPane.add(l_mt_add,0,1);
            gridPane.add(tf_mt_add,1,1);
        }
        else if(type.equals(EventType.PlayerChange) || type.equals(EventType.Foul))
        {
            l_player1.setText(type.equals(EventType.PlayerChange) ? "Player Out" : "Injured Player");
            l_player2.setText(type.equals(EventType.PlayerChange) ? "Player In" : "Foul Player");

            gridPane.add(l_player1,0,1);
            gridPane.add(cb_player1,1,1);
            gridPane.add(l_player2,0,2);
            gridPane.add(cb_player2,1,2);
        }
    }

    public JFXComboBox<String> getPlayersCB()
    {
        JFXComboBox<String> cb_player = new JFXComboBox<>();
        cb_player.setPromptText("Choose Player");

        List<String> players = new ArrayList<>();
        for(Player player : match.getHomeTeam().getPlayers())
        {
            if(player.isActive())
            {
                players.add("" + match.getHomeTeam().getName() + "-" + player.getName());
            }
        }
        for(Player player : match.getAwayTeam().getPlayers())
        {
            if(player.isActive())
            {
                players.add("" + match.getAwayTeam().getName() + "-" + player.getName());
            }
        }

        cb_player.setItems(FXCollections.observableList(players));
        return cb_player;
    }

    public void submit()
    {
        EventType type = (EventType) cb_eventType.getValue();
        if(type == null)
        {
            showSimpleAlert("Error","Event Type Not Supported");
            return;
        }

        Player player1 = null;
        Player player2 = null;
        int matchTime = -1;
        int timeAdd = -1;

        // validate special inputs
        if (EventType.YellowCard.equals(type) || EventType.RedCard.equals(type) || EventType.Goal.equals(type) || EventType.Offside.equals(type) || EventType.Injury.equals(type))
        {
            player1 = choiceToPlayer(cb_player1);
            if(player1 == null)
            {
                showSimpleAlert("Error","Could not find player");
                return;
            }
        }
        else if(EventType.Foul.equals(type) || EventType.PlayerChange.equals(type))
        {
            player1 = choiceToPlayer(cb_player1);
            player2 = choiceToPlayer(cb_player2);
            if(player1 == null || player2 == null)
            {
                showSimpleAlert("Error","Could not find Player");
                return;
            }
            if(player1.equals(player2))
            {
                showSimpleAlert("Error","Players must be different");
                return;
            }
        }
        else if(EventType.TimeStop.equals(type))
        {
            try {
                timeAdd = Integer.parseInt(tf_mt_add.getText());
            }
            catch (Exception e)
            {
                showSimpleAlert("Error","Time To Add must be Integer");
                return;
            }
            if(timeAdd < 0)
            {
                showSimpleAlert("Error","Time To Add must be positive");
            }
        }
        // validate time input
        try {
            matchTime = Integer.parseInt(tf_mt.getText());
            if(matchTime < 0)
            {
                showSimpleAlert("Error","Match Time must be positive");
            }
        }
        catch (Exception e)
        {
            showSimpleAlert("Error","Match Time must be Integer");
            return;
        }

        // create and update event log
        switch (type)
        {
            case YellowCard: unit.addYellowCard(ClientSystem.getLoggedUser(),match,player1,matchTime); break;
            case RedCard: unit.addRedCard(ClientSystem.getLoggedUser(),match,player1,matchTime); break;
            case Goal: unit.addGoal(ClientSystem.getLoggedUser(),match,player1,matchTime); break;
            case Offside: unit.addOffside(ClientSystem.getLoggedUser(),match,player1,matchTime); break;
            case PlayerChange: unit.addPlayerChange(ClientSystem.getLoggedUser(),match,player1,player2,matchTime); break;
            case Injury: unit.addInjury(ClientSystem.getLoggedUser(),match,player1,matchTime); break;
            case Foul: unit.addFoul(ClientSystem.getLoggedUser(),match,player1,player2,matchTime); break;
            case TimeStop: unit.addStoppageTime(ClientSystem.getLoggedUser(),match,matchTime,timeAdd); break;
            case EndGame: unit.addEndGame(ClientSystem.getLoggedUser(),match,matchTime); break;
            case PenaltyKick: unit.addPenaltyKick(ClientSystem.getLoggedUser(),match,matchTime); break;
            default: showSimpleAlert("Error","Event Type Not Supported"); return;
        }

        // clean and return
        eventLogController.refreshTable();
        App.mainStage.setScene(App.scenes.pop());
    }

    private Player choiceToPlayer(ComboBox cb_player)
    {
        String temp = (String)cb_player.getValue();
        if(temp == null) return null;
        String[] val = temp.split("-");
        Team team = match.getHomeTeam().getName().equals(val[0]) ? match.getHomeTeam() : match.getAwayTeam();
        Player result = null;
        for(int i = 0; i < team.getPlayers().size() && result == null; i++)
        {
            Player p = team.getPlayers().get(i);
            if(p.isActive() && p.getName().equals(val[1])) result = p;
        }

        return result;
    }

    public void closeWindow()
    {
        App.mainStage.setScene(App.scenes.pop());
    }
}
