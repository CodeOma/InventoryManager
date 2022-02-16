/**
 *Modify Product Con
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 * @see         Image
 */

package Main.Controllers;

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


public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

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


    /** search parts table */
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


    /** selected product's associated parts */
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



    private ObservableList<Part> AssociatedParts = FXCollections.observableArrayList();
    public void getProduct(Product product){

        idTextBox.setText(String.valueOf(product.getId()));
        idTextBox.setEditable(false);
        nameTextBox.setText(String.valueOf(product.getName()));
        inventoryCountTextBox.setText(String.valueOf(product.getStock()));
        priceTextBox.setText(String.valueOf(product.getPrice()));
        maxTextBox.setText(String.valueOf(product.getMax()));
        minTextBox.setText(String.valueOf(product.getMin()));

        AssociatedParts.addAll(product.getAssociatedParts());

        selectedPartsTable.setItems(AssociatedParts);
        selectedPartIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        selectedPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        selectedPartInventoryCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        selectedPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
    }

    /** Search Parts*/
    public void searchParts(){
        if (!partSearchBox.getText().trim().isEmpty()) {
            partsTable.setItems(Inventory.searchParts(partSearchBox.getText().trim()));
        }else{
            partsTable.setItems(Inventory.getAllParts());
        }
        selectedPartsTable.refresh();
        }

    /** Add Associated Part */
    public void addPartToProduct() {
        Product product = Inventory.getProduct(Integer.parseInt(idTextBox.getText()));
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        try{
            if(selectedPart != null) {
                product.addAssociatedPart(selectedPart);
                AssociatedParts.add(selectedPart);
            }
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select part to add.");
            alert.show();
        }
    }


    public void removePart() {
        try {
            Product product = Inventory.getProduct(Integer.parseInt(idTextBox.getText()));
            Part selectedPart = Inventory.getPart(selectedPartsTable.getSelectionModel().getSelectedItem().getId());
            String selectedPartName = String.valueOf(selectedPartsTable.getSelectionModel().getSelectedItem().getName());
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " +
                    selectedPartName + " from " + product.getName() +
                    "? ");
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AssociatedParts.remove(selectedPart);
                product.removeAssociatedPart(selectedPart.getId());
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No part selected:");
            alert.setContentText("Please select an item from the available product list to add.");
            alert.show();
        }
    }

    /** save Product */
    public void saveProduct(ActionEvent event) throws IOException {
        try {

            int id = Integer.parseInt(idTextBox.getText());
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
                /** Modify Product */
                Inventory.modifyProduct(id, new Product(id, name, price, inventoryCount, min, max));
                /** to MainMenu */
                stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
                scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

            } catch (Exception exception) {
            System.out.println(exception.toString().contains("For input string"));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if(exception.toString().contains("For input string")){
                alert.setContentText("Please check entries for Inventory, Max and Min are Integers\n Price must be number");
            }else{
                alert.setContentText(exception.getMessage());
            }
                alert.show();
            }
    }

    /** Initialize Table */
    public  void initialize(URL url, ResourceBundle rb){
        partsTable.setItems(Inventory.getAllParts());
        partsTableIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        partsTableNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partsTableInventoryCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        partsTablePriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
    }

    /** to MainMenu */
    public void toMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
