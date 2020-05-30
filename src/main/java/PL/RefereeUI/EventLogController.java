package PL.RefereeUI;

import BL.Client.ClientSystem;
import BL.Client.Handlers.MatchEventUnit;
import DL.Game.Match;
import DL.Game.MatchEvents.Event;
import PL.main.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static PL.AlertUtil.showSimpleAlert;

/**
 * Created By: Assaf, On 04/05/2020
 * Description:
 */
public class EventLogController
{
    @FXML
    public TableView<EventView> tv_events;
    public Label lb_match;

    private MatchEventUnit unit;
    private Match match;
    private Scene parent;

    public class EventView
    {
        private final SimpleStringProperty eventType;
        private final SimpleStringProperty eventInfo;
        private final SimpleIntegerProperty eventGameTime;
        private final Button removeButton;

        public EventView(Event event)
        {
            eventType = new SimpleStringProperty(event.getType());
            eventInfo = new SimpleStringProperty(event.toString());
            eventGameTime = new SimpleIntegerProperty(event.getEventGameTime());

            removeButton = new Button("Remove");
            removeButton.onActionProperty().set(ev -> {
                try {
                    if(!unit.removeEvent(ClientSystem.getLoggedUser(),match,event))
                    {
                        showSimpleAlert("Error", "Could not remove Event");
                    }
                    else refreshTable();
                } catch (Exception e) {
                    showSimpleAlert("Error", "Could not remove Event");
                    e.printStackTrace();
                }
            });
        }

        public Button getRemoveButton() {
            return removeButton;
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

    public void initialize(MatchEventUnit unit, Match match)
    {
        this.unit = unit;
        this.match = match;
        this.parent = App.mainStage.getScene();

        lb_match.setText(match.getHomeTeam().getName() + " VS " + match.getAwayTeam().getName());

        tv_events.prefHeightProperty().bind(App.mainStage.heightProperty());
        tv_events.prefWidthProperty().bind(App.mainStage.widthProperty());

        refreshTable();
    }

    public void refreshTable()
    {
        // Get Info
        tv_events.setVisible(false);
        List<EventView> viewList = new ArrayList<>();
        for(Event event : match.getMyEventLog().getEvents())
        {
            viewList.add(new EventView(event));
        }

        tv_events.getColumns().clear();

        TableColumn tc_eventType = new TableColumn("Event Type");
        tc_eventType.setCellValueFactory(new PropertyValueFactory<EventView,String>("eventType"));
        tv_events.getColumns().add(tc_eventType);

        TableColumn tc_eventInfo = new TableColumn("Information");
        tc_eventInfo.setCellValueFactory(new PropertyValueFactory<EventView,String>("eventInfo"));
        tv_events.getColumns().add(tc_eventInfo);

        TableColumn tc_eventGameTime = new TableColumn("Match Time (Min)");
        tc_eventGameTime.setCellValueFactory(new PropertyValueFactory<EventView,String>("eventGameTime"));
        tv_events.getColumns().add(tc_eventGameTime);

        TableColumn tc_removeButton = new TableColumn("Action");
        tc_removeButton.setCellValueFactory(new PropertyValueFactory<EventView,Button>("removeButton"));
        tv_events.getColumns().add(tc_removeButton);

        tv_events.setItems(FXCollections.observableList(viewList));
        tv_events.setVisible(true);
    }

    public void addEvent() throws IOException
    {
        AddEventController controller = (AddEventController)App.loadScreen("addEventScreen");
        controller.initialize(unit,match,this);
    }

    public void closeWindow()
    {
        App.mainStage.setScene(App.scenes.pop());
    }
}
