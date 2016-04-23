package geekhouse.ir.yumbox.data.api.models.Customer;

import geekhouse.ir.yumbox.data.api.models.feed.dailyMeals;

/**
 * Copyright (c) 2015-2016 www.Tipi.me.
 * Created by Ashkan Hesaraki.
 * Ashkan@tipi.me
 */
public class orderHistory {

    public String at;
    public int quantity;
    public String status;
    public dailyMeals daily_meal;

    public orderHistory(String at, int quantity, String status, dailyMeals daily_meal) {
        this.at = at;
        this.daily_meal = daily_meal;
        this.quantity = quantity;
        this.status = status;
    }
}
