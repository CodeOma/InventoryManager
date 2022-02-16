package Main.Controllers;
/**
 *
 * @author Omar Ahmed
 * RUNTIME ERROR: trying to click edit without selecting an item. Corrected this by providing
 * an error message
 * FUTURE ENHANCEMENT: Autocomplete search
 *
 */

import Main.Main;
import Main.Models.Inventory;
import Main.Models.Part;
import Main.Models.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    Stage stage;
    Parent scene;
//    Inventory inv;

/** Search boxes */
    @FXML
    private TextField partSearchBox;
    @FXML
    private TextField productSearchBox;

    /** Create Tables */

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    /** Generate */
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();

    /**
     * Initializes controller class.
     */

/** Search */
/** Search for Parts */
    public void searchParts() {
        if (!partSearchBox.getText().trim().isEmpty()) {
            partTable.setItems(Inventory.searchParts(partSearchBox.getText().trim()));
        }else{
            partTable.setItems(partInventory);
        }
        partTable.refresh();
    }

/** Search for Products */
    public void searchProducts() {
        try {
            if (!productSearchBox.getText().trim().isEmpty()) {
                productTable.setItems(Inventory.searchProducts(productSearchBox.getText().trim()));
            } else {
                productTable.setItems(productInventory);
            }
            productTable.refresh();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /** delete part */
    public void deletePart() {
        try {

            Part part = partTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " +
                    part.getName() +
                    "? ");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(part);
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No part selected:");
            alert.setContentText("Please select an item from the available product list to add.");
            alert.show();
        }

    }

    /** delete product */
    public void deleteProduct() {
        try {
            Product product = productTable.getSelectionModel().getSelectedItem();
            if(product.getAssociatedParts().size() > 0){
                throw new Exception("Product cannot be deleted with parts. Please remove parts before deleting");
                }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " +
                    product.getName() +
                    "? ");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(product);
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if(e.toString().contains("cannot be deleted")){
                alert.setContentText(e.getMessage().toString());
            }else{
                alert.setHeaderText("No part selected:");
                alert.setContentText("Please select a product to delete.");
            }

            alert.show();
        }

    }
/** Exit app */
    public void onExit(ActionEvent event) {

        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        stage.close();

    }


    /** Navigation to other pages */

    /** To Add Part **/
    public void toAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** To Add Product **/
    public void toAddProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** To Add Product **/
    public void toModifyPart(ActionEvent event) throws IOException {
        try {
            stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("ModifyPart.fxml"));
            fxmlLoader.load();

            ModifyPartController ModifyController = fxmlLoader.getController();
            /** Passes the selected part to the Modify Parts Page*/
            ModifyController.getPart(partTable.getSelectionModel().getSelectedItem());
            System.out.println("err");

            System.out.println(partTable.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("No Part Selected to Modify.");
            alert.setContentText("Please select an item to modify");
            alert.show();
        }
    }

    /** To Add Modify**/
    public void toModifyProduct(ActionEvent event) throws IOException {
        try {

            stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("ModifyProduct.fxml"));
            fxmlLoader.load();

            ModifyProductController ModifyController = fxmlLoader.getController();

            ModifyController.getProduct(productTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a product to modify");
            alert.show();
        }
    }

    public  void initialize(URL url, ResourceBundle rb){
        /**
     * Get Parts from allParts list and Set table columns to appropriate values
     */
        partInventory.setAll(Inventory.getAllParts());
        partTable.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        partNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        partPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

    /**
     * Get Products from allProducts list and Set table columns to appropriate values
     */
        productInventory.setAll(Inventory.getAllProducts());
        productTable.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        productPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
    }


}

