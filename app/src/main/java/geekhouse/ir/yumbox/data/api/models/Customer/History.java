package geekhouse.ir.yumbox.data.api.models.Customer;

import java.util.List;

import geekhouse.ir.yumbox.data.api.models.feed.dailyMeals;
import geekhouse.ir.yumbox.ui.OrderHistory;

/**
 * Copyright (c) 2015-2016 www.Tipi.me.
 * Created by Ashkan Hesaraki.
 * Ashkan@tipi.me
 */
public class History {

  public List<orderHistory> data;
  public String error;

  public History (List<orderHistory> data, String error) {
    this.data = data;
    this.error = error;
  }
}
