package pl.karasdominik.chessgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class chessApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Chessboard chessboard = new Chessboard();
        Scene scene = new Scene(chessboard, 750, 650);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}