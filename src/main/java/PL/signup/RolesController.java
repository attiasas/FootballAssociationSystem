package PL.signup;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j
public class RolesController implements Initializable {

    public AnchorPane rolesContainer;
    @FXML
    private StackPane rootAnchorPane;

    @FXML
    private Button btnOwner;

    @FXML
    private Button btnChairman;

    @FXML
    private Button btnManager;

    @FXML
    private Button btnPlayer;

    @FXML
    private Button btuReferee;

    @FXML
    private Button btnAssociation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void closeStage() {
        ((Stage) rootAnchorPane.getScene().getWindow()).close();
    }

    public void loadMain(javafx.event.ActionEvent actionEvent) {
        loadPanel("MainApp");
    }

    public void backToMain() {
        loadPanel("MainApp");
    }

    public void loadStage(String fxmlFileName) {
        try {
            closeStage();
            Parent parent = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setTitle("Sportify");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            log.info(ex.getMessage());
        }
    }

    public void loadPanel(String fxmlFileName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = rolesContainer.getScene();
        assert root != null;
        root.translateXProperty().set(scene.getWidth());

        rootAnchorPane.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            rootAnchorPane.getChildren().remove(rolesContainer);
        });
        timeline.play();
    }
}
