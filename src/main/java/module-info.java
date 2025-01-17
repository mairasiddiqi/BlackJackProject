module com.example.blackjackproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens blackJackGame to javafx.fxml;
    exports blackJackGame;
}
