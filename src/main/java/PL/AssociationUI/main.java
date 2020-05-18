package PL.AssociationUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Loading Main.Main Window
        primaryStage.setTitle("AssociationUnit");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Window/AssociationUnitFXML.fxml").openStream());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        AssociationController a = fxmlLoader.getController();
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.show();

    }
}