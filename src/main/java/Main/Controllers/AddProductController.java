package Main.Controllers;
/**
 *
 * @author Omar Ahmed
 * RUNTIME ERROR: Form requires correct String and Int types. Used error exception to lets users know
 * to enter correct format.
 * FUTURE ENHANCEMENT: Provide a unique message for each error
 *
 */

import Main.Main;
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

import Main.Models.Inventory;
import Main.Models.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddProductController implements Initializable {
    /**Prep Stage */
    Stage stage;
    Parent scene;

    /**Import elements from FXML that will be used*/
    @FXML
    private TextField idTextBox;
    @FXML
    private TextField nameTextBox;
    @FXML
    private TextField inventoryCountTextBox;
    @FXML
    private TextField priceTextBox;
    @FXML
    private TextField maxTextBox;
    @FXML
    private TextField minTextBox;


    /** Search Parts table */
    @FXML
    private TextField partSearchBox;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partsTableIdColumn;
    @FXML
    private TableColumn<Part, String> partsTableNameColumn;
    @FXML
    private TableColumn<Part, Integer> partsTableInventoryCountColumn;
    @FXML
    private TableColumn<Part, Double> partsTablePriceColumn;


    /** Associated product's table */
    @FXML
    private TableView<Part> selectedPartsTable;
    @FXML
    private TableColumn<Part, Integer> selectedPartIdColumn;
    @FXML
    private TableColumn<Part, String> selectedPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> selectedPartInventoryCountColumn;
    @FXML
    private TableColumn<Part, Double> selectedPartPriceColumn;


    /**Initialize the Associated parts list*/
    private ObservableList<Part> AssociatedParts = FXCollections.observableArrayList();


    /**Search parts*/
    public void searchParts(){
        if (!partSearchBox.getText().trim().isEmpty()) {
            partsTable.setItems(Inventory.searchParts(partSearchBox.getText().trim()));
        }else{
            partsTable.setItems(Inventory.getAllParts());
        }
        selectedPartsTable.refresh();
    }

    /**Add a part to a product*/
    public void addPartToProduct() {
        /**Fetch product*/
        Product product = Inventory.getProduct(Integer.parseInt(idTextBox.getText()));
        /**Fetch selected part*/
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        try{
            /** If Not Null add part*/
            if(selectedPart != null) {
                product.addAssociatedPart(selectedPart);
                AssociatedParts.add(selectedPart);
            }
            /** If Null, it means there is no selection. So we throw an Error*/
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select part to add.");
            alert.show();
        }
    }

    /**Remove a part to a product*/
    public void removePart() {
        try {
            /**Fetch product*/
            Product product = Inventory.getProduct(Integer.parseInt(idTextBox.getText()));
            /**Fetch selected part*/
            Part selectedPart = Inventory.getPart(selectedPartsTable.getSelectionModel().getSelectedItem().getId());
            String selectedPartName = String.valueOf(selectedPartsTable.getSelectionModel().getSelectedItem().getName());
            /**Asks user to confirm they would like to remove with an Alert window*/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " +
                    selectedPartName + " from " + product.getName() +
                    "? ");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AssociatedParts.remove(selectedPart);
                product.removeAssociatedPart(selectedPart.getId());
            }

        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No part selected:");
            alert.setContentText("Please select an item from the available product list to add.");
            alert.show();
        }
    }

    public void saveProduct(ActionEvent event) throws IOException {
        try {

            int id = Inventory.generateID();
            int inventoryCount = Integer.parseInt(inventoryCountTextBox.getText());
            int min = Integer.parseInt(minTextBox.getText());
            int max = Integer.parseInt(maxTextBox.getText());
            double price = Double.parseDouble(priceTextBox.getText());
            String name = nameTextBox.getText();

            if (min > max) {
                throw new Exception("Min has to be less than Max");
            }
            if (min > inventoryCount || inventoryCount > max) {
                throw new Exception("Inventory count must be within Min and Max ranges");
            }
            if (min < inventoryCount && inventoryCount < max) {
                Inventory.addProduct(new Product(id, name, price, inventoryCount, min, max));
                stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
                scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(exception.toString().contains("For input string")){
                alert.setContentText("Please check entries for Inventory, Max and Min are Integers\n Price must be number");
            }else{
                alert.setContentText(exception.getMessage());
            }
            alert.show();
        }
    }


    public  void initialize(URL url, ResourceBundle rb){
        /**Initialize Parts */
        partsTable.setItems(Inventory.getAllParts());
        partsTableIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        partsTableNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partsTableInventoryCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        partsTablePriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        /**Initialize Products */
        selectedPartsTable.setItems(AssociatedParts);
        selectedPartIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        selectedPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        selectedPartInventoryCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        selectedPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
    }

    /**To MainMenu */
    public void toMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
