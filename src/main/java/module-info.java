module com.example.blackjackproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens blackJackGame to javafx.fxml;
    exports blackJackGame;
}
