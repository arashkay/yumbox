package geekhouse.ir.yumbox.data.api.models.feed;

import java.util.List;

/**
 * Created by A on 4/15/2016.
 */
public class feed {

    public List<dailyMeals> data;
    public String error;

    public feed (List<dailyMeals> data, String error) {
        this.data = data;
        this.error = error;
    }
}
