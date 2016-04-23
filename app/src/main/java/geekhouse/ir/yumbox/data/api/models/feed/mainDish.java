package geekhouse.ir.yumbox.data.api.models.feed;

/**
 * Created by A on 4/15/2016.
 */
public class mainDish {

    public String doc_key;
    public String name;
    public String contains;
    public String description;
    public int price;
    public int calories;

    public mainDish(String doc_key, String name, String contains, String description,
                    int price, int calories) {
        this.doc_key = doc_key;
        this.name = name;
        this.contains = contains;
        this. description = description;
        this.price = price;
        this.calories = calories;
    }
}
