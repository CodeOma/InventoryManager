package Main;

import Main.Models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Inventory inv = new Inventory();
        addData(inv);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 480);
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
    private void addData(Inventory inv){
        /** InHouse Parts */
        Part A = new InHouse(101, "Gear", 7.99, 24, 1, 100, 1001);
        Part B = new InHouse(201, "Tire", 12.99, 12, 1, 100, 1012);
        inv.addPart(A);
        inv.addPart(B);
        /** OutSourced Parts */
        Part C = new OutSourced(301, "Chain", 12.99, 23, 10, 100, "Company A");
        Part D = new OutSourced(401, "Pedal", 3.99, 12, 12, 100, "Company B");
        inv.addPart(C);
        inv.addPart(D);
        /** allProducts */
        Product prod1 = new Product(1001, "Fuji Bike", 99.99, 20, 10, 100);
        prod1.addAssociatedPart(A);
        prod1.addAssociatedPart(C);
        inv.addProduct(prod1);
        Product prod2 = new Product(2001, "Tricycle", 59.99, 30, 10, 100);
        prod2.addAssociatedPart(B);
        prod2.addAssociatedPart(D);
        prod2.addAssociatedPart(C);
        inv.addProduct(prod2);
        Product prod3 = new Product(3001, "Unicycle", 29.99, 20, 10, 100);
        prod3.addAssociatedPart(A);
        inv.addProduct(prod3);
        Product prod4 = new Product(4001, "ATV", 199.99, 23, 10, 100);
        prod4.addAssociatedPart(D);
        inv.addProduct(prod4);

    }

}
