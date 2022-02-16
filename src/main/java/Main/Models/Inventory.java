package Main.Models;

/**
 *
 * @author Omar Ahmed
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> selectedParts = FXCollections.observableArrayList();

    /** To Add Part*/
    public static void addPart(Part part){
        allParts.add(part);
    }
    /** To Add Product*/
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /** Fetch */
    public static Part getPart(int id){
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == id){
                return allParts.get(i);
            }
        }
        return null;
    }

    public static Product getProduct(int id){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == id){
                return allProducts.get(i);
            }
        }
        return null;
    }


    /** Search*/
    /** Searching Parts **/
    public static Part findPartById(int partId){
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == partId){
                return allParts.get(i);
            }
        }
        return null;
    }

    public static ObservableList<Part> searchParts(String search){
        System.out.println(search);
        ObservableList<Part> filteredParts =  FXCollections.observableArrayList();
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getName().toLowerCase().contains(search.toLowerCase())){
                filteredParts.add(allParts.get(i));
            }else if (String.valueOf(allParts.get(i).getId()).toLowerCase().contains(search.toLowerCase())){
                filteredParts.add(allParts.get(i));
            }

        }
        if(!(filteredParts.isEmpty())){
            return filteredParts;
        }else{
            return null;
        }
    }


    /** Searching Products **/
    public static Product findProductById(int id){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == id){
                return allProducts.get(i);
            }
        }
        return null;
    }

    public static ObservableList<Product> searchProducts(String search){
        System.out.println(search);
        ObservableList<Product> filteredProducts =  FXCollections.observableArrayList();

        for(int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName().toLowerCase().contains(search.toLowerCase())) {
                filteredProducts.add(allProducts.get(i));
            } else if (String.valueOf(allProducts.get(i).getId()).toLowerCase().contains(search.toLowerCase())){
                filteredProducts.add(allProducts.get(i));
        }
        }

        if(!(filteredProducts.isEmpty())){
            return filteredProducts;
        }
        else{
            return null;
        }
    }


    public static void modifyPart(int id, Part selectedPart){
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == id){
                Inventory.getAllParts().set(i, selectedPart);
            }
        }
    }

    public static void modifyProduct(int id,Product selectedProduct){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == id){
                allProducts.get(i).setName(selectedProduct.getName());
                allProducts.get(i).setPrice(selectedProduct.getPrice());
                allProducts.get(i).setInventoryCount(selectedProduct.getStock());
                allProducts.get(i).setMin(selectedProduct.getMin());
                allProducts.get(i).setMax(selectedProduct.getMax());
            }
        }

    }


    public static boolean deletePart(Part selectedPart){
        if(allParts.remove(selectedPart)){
            return true;
        }
        return false;
    }

    public static boolean deleteProduct(Product selectedProduct){
        if(allProducts.remove(selectedProduct)){
            return true;
        }
        return false;
    }

    public static boolean deleteSelectedPart(Part selectedPart){
        return selectedParts.remove(selectedPart);
    }

    public static ObservableList<Part> getAllParts(){
        return allParts;
    }


    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    public static ObservableList<Part> getSelectedParts(){
        return selectedParts;
    }


    public static int generateID(){
        Random ran = new Random();
        return ran.nextInt(3000) + 1000;
    }

}
