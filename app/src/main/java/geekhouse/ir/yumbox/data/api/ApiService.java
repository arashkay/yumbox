package geekhouse.ir.yumbox.data.api;

import geekhouse.ir.yumbox.data.api.models.Customer.Customer;
import geekhouse.ir.yumbox.data.api.models.Customer.History;
import geekhouse.ir.yumbox.data.api.models.Success;
import geekhouse.ir.yumbox.data.api.models.feed.feed;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

  @GET("app/menu")
  Call<feed> getFeed();

  @FormUrlEncoded
  @POST("app/orders")
  Call<Success> signUp(@Field("quantity") int quantity,
                        @Field("daily_meal_key") String daily_meal_key,
                        @Field("customer[]") Customer customers,
                        @Header("Authorization") String authorization);

  @GET("app/orders")
  Call<History> getOrderHistory(@Header("Authorization") String authorization);

}