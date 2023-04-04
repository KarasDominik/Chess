module pl.karasdominik.chessgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.karasdominik.chessgame to javafx.fxml;
    exports pl.karasdominik.chessgame;
}