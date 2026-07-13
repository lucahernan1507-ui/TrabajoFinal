package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/application/Main.fxml"));

        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource("/application/application.css")
                        .toExternalForm());

        stage.setTitle("Ajedrez FX");

        stage.setWidth(900);
        stage.setHeight(700);

        stage.setResizable(false);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}