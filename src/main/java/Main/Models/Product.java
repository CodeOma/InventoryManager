package Main.Models;
/**
 *
 * @author Omar Ahmed
 */
import java.util.ArrayList;


public class Product {

    private int id;
    private String name;
    private double price = 0.0;
    private int stock = 0;
    private int min;
    private int max;
    private double cost;
    private ArrayList<Part> associatedParts = new ArrayList<Part>();

    public Product(int id, String name, double price, int inventoryCount, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setInventoryCount(inventoryCount);
        setMin(min);
        setMax(max);

    }

    public ArrayList<Part> getAssociatedParts() {
        return associatedParts;
    }

    public void setAssociatedParts(ArrayList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setInventoryCount(int count) {
        this.stock = count;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void addAssociatedPart(Part partToAdd) {
        associatedParts.add(partToAdd);
    }

    public boolean removeAssociatedPart(int id) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == id) {
                associatedParts.remove(i);
                return true;
            }
        }

        return false;
    }

    public Part lookupAssociatedPart(int id) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == id) {
                return associatedParts.get(i);
            }
        }
        return null;
    }

    public int getPartsListSize() {
        return associatedParts.size();
    }

}
