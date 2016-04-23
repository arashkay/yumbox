package geekhouse.ir.yumbox.data.api.models.feed;

import java.util.List;

/**
 * Created by A on 4/15/2016.
 */
public class dailyMeals {

    public String doc_key;
    public int total;
    public int remained;
    public String at;
    public mainDish main_dish;
    public List<sideDishes> side_dishes;

    public dailyMeals(String doc_key, int total, mainDish main_dish, List<sideDishes> side_dishes
    , int remained, String at) {
        this.doc_key = doc_key;
        this.total = total;
        this.main_dish = main_dish;
        this.side_dishes = side_dishes;
        this.remained = remained;
        this.at = at;
    }
}
