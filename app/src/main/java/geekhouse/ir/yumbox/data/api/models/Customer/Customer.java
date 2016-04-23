package geekhouse.ir.yumbox.data.api.models.Customer;

/**
 * Copyright (c) 2015-2016 www.Tipi.me.
 * Created by Ashkan Hesaraki.
 * Ashkan@tipi.me
 */
public class Customer {

  public String address;
  public String name;
  public String mobile;

  public Customer(String mobile, String address, String name) {
    this.mobile = mobile;
    this.address = address;
    this.name = name;
  }

}
