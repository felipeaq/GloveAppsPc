package javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String filePathString ="res/fxml/MainScreen.fxml";




        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));

        Scene scene = new Scene(root);

        String css = getClass().getResource("/fxml/css/css.css").toExternalForm();

        scene.getStylesheets().addAll(css);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("SmartGlove PC App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
