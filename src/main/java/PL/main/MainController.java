package PL.main;

import BL.Client.ClientSystem;
import DL.Users.User;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import io.airlift.command.Cli;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static PL.AlertUtil.showSimpleAlert;


public class MainController implements Initializable {

    private static final String NOT_AVAILABLE = "Not Available";
    private static final String NO_SUCH_AVAILABLE = "No Such Available";
    private static final String NO_MEMBER_AVAILABLE = "No Member Available";
    private static final String AVAILABLE = "Available";
    public Tab tabOverView;
    @FXML
    public AnchorPane parentAnchorPane;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTabPane mainTabPane;

    @FXML
    public Label l_welcome;
    @FXML
    public TextField t_query;
    @FXML
    public ComboBox cb_searchType;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        if(ClientSystem.getLoggedUser() != null)
        {
            l_welcome.setText("Welcome " + ClientSystem.getLoggedUser().getUsername() + "!");

            String hamburgerFXL = null;

            switch (ClientSystem.getLoggedUserType())
            {
                case Association: hamburgerFXL = "AssociationToolbar"; break;
                case Referee: hamburgerFXL = "RefereeToolbar"; break;
                case Fan: hamburgerFXL = "FanToolbar"; break;
                case Owner: hamburgerFXL = "TeamOwnerToolbar"; break;
                case Manager: hamburgerFXL = "TeamManagerToolbar"; break;
                case Player: hamburgerFXL = "PlayerToolbar"; break;
                case Admin: hamburgerFXL = "ManagerToolbar"; break;
                //case None: hamburgerFXL = "NoUserToolbar"; break;
            }

            if(hamburgerFXL != null)
            {
                initDrawer(hamburgerFXL);
            }
        }
        else hamburger.setVisible(false);
    }

    public void exploreLeagues()
    {
        showSimpleAlert("Error", "Not Supported, will be implemented in the future.");
    }

    public void exploreTeams()
    {
        showSimpleAlert("Error", "Not Supported, will be implemented in the future.");
    }

    public void search()
    {
        showSimpleAlert("Error", "Not Supported, will be implemented in the future.");
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    public void handleMenuFileSettings() {
        loadStage("Settings");
    }

    @FXML
    private void handleMenuAbout() {
        loadStage("About");
    }

    private void closeStage() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    public void handleMenuFileLogout() {
        closeStage();
        loadStage("Login");
    }

    @FXML
    private void handleMenuViewFullScreen() {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void handleMenuAddPlayer() {
        try {
            loadRoles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStage(String fxmlFileName) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Sportify");

            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void loadRoles() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Window/Roles.fxml"));
        Scene scene = rootAnchorPane.getScene();
        root.translateXProperty().set(scene.getWidth());

        rootAnchorPane.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> rootAnchorPane.getChildren().remove(parentAnchorPane));
        timeline.play();
    }

    private void initDrawer(String userType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/Window/hamburger/%s.fxml", userType)));
            VBox toolbar = loader.load();
            drawer.setSidePane(toolbar);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }

    /*

    TODO ------------------------------- TODO ------------------------------- TODO

     */

    @FXML
    private void loadTeamInfo(ActionEvent event) {
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
    }

    @FXML
    private void loadOperation(ActionEvent event) {
    }

    @FXML
    private void loadInfo2(ActionEvent event) {
    }

    @FXML
    private void loadSubmissionOp(ActionEvent event) {
    }

    @FXML
    private void loadAddOp(ActionEvent event) {

    }

    private void initGraphs() {

    }

    @FXML
    private void handleMenuNotification(ActionEvent event) {
    }

    public void handleMenuDonate(ActionEvent actionEvent) {

    }

    public void handleMenuAddEvent(ActionEvent actionEvent) {

    }

    public void handleMenuAddAsset(ActionEvent actionEvent) {

    }

    @FXML
    private void handleMenuViewTeams(ActionEvent event) {
    }

    @FXML
    private void handleMenuFileOpen(ActionEvent event) {
    }

    @FXML
    private void handleMenuViewPlayers(ActionEvent event) {
    }

    @FXML
    private void handleMenuViewManagers(ActionEvent event) {

    }

    public void handleMenuFileUpdate(ActionEvent actionEvent) {

    }

    public void handleMenuFileSave(ActionEvent actionEvent) {

    }

    public void handleMenuNotification()
    {
        try {
            loadNotifications();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotifications() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Window/NotificationsFXML.fxml"));

        App.mainStage.setScene(root.getScene());
    }
}
