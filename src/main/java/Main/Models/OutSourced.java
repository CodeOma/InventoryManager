package Main.Models;
/**
 *
 * @author Omar Ahmed
 */
public class OutSourced extends Part {

    private String companyName;

    public OutSourced(int id, String name, double price, int stock, int min, int max, String company) {

        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setCompanyName(company);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

}
