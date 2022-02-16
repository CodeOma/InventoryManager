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
import Main.Models.InHouse;
import Main.Models.Inventory;
import Main.Models.OutSourced;
import Main.Models.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class ModifyPartController {
    Stage stage;
    Parent scene;

    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outSourcedRadioButton;

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
    private Label manufacturerLabel;
    @FXML
    private TextField manufacturerTextBox;
    @FXML
    private TextField minTextBox;

    @FXML
    private Text errorLabel;
    /** Navigation to other pages */

    /** To Main Menu if Cancelled**/
    public void toMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)(event.getSource())).getScene().getWindow();
        scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onRadioChange() {
        if (inHouseRadioButton.isSelected()) {
            manufacturerLabel.setText("Machine ID:");
        }else if (outSourcedRadioButton.isSelected()){
            manufacturerLabel.setText("Company \nName:");
        }
    }
    /** Initializes Part info **/
    public void getPart(Part part) {
        /**
         * Get and Set each form field to reflect corresponding data of part Object
         */
        idTextBox.setText(String.valueOf(part.getId()));
        idTextBox.setEditable(false);
        nameTextBox.setText(String.valueOf(part.getName()));
        inventoryCountTextBox.setText(String.valueOf(part.getStock()));
        priceTextBox.setText(String.valueOf(part.getPrice()));
        maxTextBox.setText(String.valueOf(part.getMax()));
        minTextBox.setText(String.valueOf(part.getMin()));
//        /**
//         * Detect if selected part is InHouse Part Object - if true load MachineID field, get and set data
//         */
        if (part instanceof InHouse) {
            manufacturerTextBox.setText(String.valueOf(((InHouse) part).getMachineId()));
            inHouseRadioButton.setSelected(true);
        }
        /**
         * Detect if selected part is Outsourced Part Object - if true load MachineID field, get and set data
         */
        else if (part instanceof OutSourced) {
            manufacturerTextBox.setText(String.valueOf(((OutSourced) part).getCompanyName()));
            manufacturerLabel.setText("Company \nname:");
            outSourcedRadioButton.setSelected(true);
        }


    }

    /**Save Part */

    public void savePart( ActionEvent event){
        try {
            /**Convert input fields into correct types*/

            int id = Inventory.generateID();
            int inventoryCount = Integer.parseInt(inventoryCountTextBox.getText());
            int min = Integer.parseInt(minTextBox.getText());
            int max = Integer.parseInt(maxTextBox.getText());
            double price = Double.parseDouble(priceTextBox.getText());
            String name = nameTextBox.getText();

            /**If inHouse selected, we use inHouse Class*/

                int manufacturerID = Integer.parseInt(manufacturerTextBox.getText());

                /**checks Min is less than Max if not throws Exception*/
                if (min > max) {
                    throw new Exception("Min has to be less than Max");
                }
                /**Checks if inventory within Min and Max values if not throws Exception */
                if (min > inventoryCount || inventoryCount > max) {
                    throw new Exception("Inventory count must be within Min and Max ranges");
                }
                if (name.length()== 0) {
                    throw new Exception("Name can't be empty");
                }
                if (min < 0 || max < 0) {
                    throw new Exception("Min and Max can't be negative");
                }

                /**If within parameters, we create new Part Instance and go back to MainMenu*/
                    errorLabel.setText("");
            if (inHouseRadioButton.isSelected()) {
                    Inventory.addPart(new InHouse(id, name, price, inventoryCount, min, max, manufacturerID));
                    stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
                    scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                /**If inHouse selected, we use inHouse Class*/
            }else if (outSourcedRadioButton.isSelected()) {
                String companyName = manufacturerTextBox.getText();
                    Inventory.addPart(new OutSourced(id, name, price, inventoryCount, min, max, companyName));
                    stage = (Stage) ((Button) (event.getSource())).getScene().getWindow();
                    scene = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
            }

            /**If there is any error we catch it and print it out*/
        } catch (Exception e) {
            Alert invalidEntryAlert = new Alert(Alert.AlertType.ERROR);
            invalidEntryAlert.setTitle("Error");
            /**If there is integer in message it is due to an invalid entry*/
            if(e.toString().contains("integer")){
                invalidEntryAlert.setContentText("Make sure the inputs are valid");
                errorLabel.setText("Error: please review correct entry types");
                /**Else*/
            }else  if(e.toString().contains("java.lang")){
                invalidEntryAlert.setHeaderText("Error: please check fields");
                errorLabel.setText("Error: please check fields");
            } else{
                invalidEntryAlert.setHeaderText(e.toString());
                errorLabel.setText(e.toString());
            }
            invalidEntryAlert.show();
        }
    }}


