module com.example.hellofx {
    requires javafx.controls;
    requires javafx.fxml;


    opens Main to javafx.fxml;
    exports Main;
    exports Main.Controllers;
    opens Main.Controllers to javafx.fxml;
}