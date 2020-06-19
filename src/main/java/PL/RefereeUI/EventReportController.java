package PL.RefereeUI;

import BL.Client.ClientSystem;
import BL.Client.Handlers.MatchEventUnit;
import DL.Game.Match;
import DL.Game.MatchEvents.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static PL.AlertUtil.showSimpleAlert;

/**
 * Created By: Assaf, On 19/06/2020
 * Description:
 */
public class EventReportController
{
    @FXML
    public Label lb_match;
    public Label l_hTeam,l_aTeam,l_date,l_time;
    public Label l_h_goals,l_h_fouls,l_h_red,l_h_yellow,l_h_off,l_h_injury,l_h_pc;
    public Label l_a_goals,l_a_fouls,l_a_red,l_a_yellow,l_a_off,l_a_injury,l_a_pc;
    public TableView tv_events;

    private Match match;
    private MatchEventUnit unit;

    public class EventView
    {
        private final SimpleStringProperty eventType;
        private final SimpleStringProperty eventInfo;
        private final SimpleIntegerProperty eventGameTime;

        public EventView(Event event)
        {
            eventType = new SimpleStringProperty(event.getType());
            eventInfo = new SimpleStringProperty(event.toString());
            eventGameTime = new SimpleIntegerProperty(event.getEventGameTime());
        }

        public String getEventType() {
            return eventType.get();
        }

        public SimpleStringProperty eventTypeProperty() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType.set(eventType);
        }

        public String getEventInfo() {
            return eventInfo.get();
        }

        public SimpleStringProperty eventInfoProperty() {
            return eventInfo;
        }

        public void setEventInfo(String eventInfo) {
            this.eventInfo.set(eventInfo);
        }

        public int getEventGameTime() {
            return eventGameTime.get();
        }

        public SimpleIntegerProperty eventGameTimeProperty() {
            return eventGameTime;
        }

        public void setEventGameTime(int eventGameTime) {
            this.eventGameTime.set(eventGameTime);
        }
    }

    public void init(MatchEventUnit unit, Match match)
    {
        this.unit = unit;
        this.match = match;

        Map matchResult = unit.getMatchResultsFromEvents(match);

        // set general labels
        lb_match.setText("(" + match.getHomeTeam().getName() +") " + matchResult.get("homeScore") + " - " + matchResult.get("awayScore") + " (" + match.getAwayTeam().getName() + ")");
        l_hTeam.setText(match.getHomeTeam().getName());
        l_aTeam.setText(match.getAwayTeam().getName());
        l_date.setText(match.getStartTime().toString());

        // set statistic labels
        l_time.setText("" + matchResult.get("matchTotalTime"));

        l_h_goals.setText("" + matchResult.get("homeScore"));
        l_h_fouls.setText("" + matchResult.get("homeFouls"));
        l_h_red.setText("" + matchResult.get("homeReds"));
        l_h_yellow.setText("" + matchResult.get("homeYellows"));
        l_h_off.setText("" + matchResult.get("homeOffs"));
        l_h_injury.setText("" + matchResult.get("homeInjury"));
        l_h_pc.setText("" + matchResult.get("homePlayerChange"));

        l_a_goals.setText("" + matchResult.get("awayScore"));
        l_a_fouls.setText("" + matchResult.get("awayFouls"));
        l_a_red.setText("" + matchResult.get("awayReds"));
        l_a_yellow.setText("" + matchResult.get("awayYellows"));
        l_a_off.setText("" + matchResult.get("awayOffs"));
        l_a_injury.setText("" + matchResult.get("awayInjury"));
        l_a_pc.setText("" + matchResult.get("awayPlayerChange"));

        // set events
        setEventTable();
    }

    private void setEventTable()
    {
        tv_events.setVisible(false);
        List<EventView> viewList = new ArrayList<>();
        for(Event event : match.getMyEventLog().getEvents())
        {
            viewList.add(new EventView(event));
        }

        tv_events.getColumns().clear();

        TableColumn tc_eventType = new TableColumn("Event Type");
        tc_eventType.setCellValueFactory(new PropertyValueFactory<EventLogController.EventView,String>("eventType"));
        tv_events.getColumns().add(tc_eventType);

        TableColumn tc_eventInfo = new TableColumn("Information");
        tc_eventInfo.setCellValueFactory(new PropertyValueFactory<EventLogController.EventView,String>("eventInfo"));
        tv_events.getColumns().add(tc_eventInfo);

        TableColumn tc_eventGameTime = new TableColumn("Match Time (Min)");
        tc_eventGameTime.setCellValueFactory(new PropertyValueFactory<EventLogController.EventView,String>("eventGameTime"));
        tv_events.getColumns().add(tc_eventGameTime);

        tv_events.setItems(FXCollections.observableList(viewList));
        tv_events.setVisible(true);
    }
}
