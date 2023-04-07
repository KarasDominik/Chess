package pl.karasdominik.chessgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chessApplication extends Application {

    private static Chessboard chessboard;
    private static List<String> moves;

    @Override
    public void start(Stage stage) throws IOException {

        chessboard = new Chessboard();
        moves = new ArrayList<>();
        Scene scene = new Scene(chessboard, 850, 700);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static Chessboard getChessboard(){
        return chessboard;
    }

    public static void main(String[] args) {
        launch();
    }
}