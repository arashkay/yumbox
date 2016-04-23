package geekhouse.ir.yumbox.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.YumboxApp;
import geekhouse.ir.yumbox.data.api.ApiService;
import geekhouse.ir.yumbox.data.api.models.Customer.History;
import geekhouse.ir.yumbox.data.api.models.Customer.orderHistory;
import geekhouse.ir.yumbox.ui.adapters.RVAdapter;
import geekhouse.ir.yumbox.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistory extends AppCompatActivity {

  @Inject ApiService apiService;

  private List<orderHistory> orders;
  private RecyclerView rv;
  private Context context = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_order_history);

    ((YumboxApp) getApplication()).getComponent().inject(this);

    rv = (RecyclerView) findViewById(R.id.rv);

    LinearLayoutManager llm = new LinearLayoutManager(this);
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeData();

    TypefaceHelper.typeface(this);
  }

  private void initializeData() {
    if (Constants.token != null && !Constants.token.isEmpty())
      apiService.getOrderHistory(Constants.token).enqueue(new Callback<History>() {
        @Override
        public void onResponse(Call<History> call, Response<History> response) {
          try {
            if (response.body().error == null) {
              orders = new ArrayList<>();
              if (response.body().data.size() > 0) {
                for (int i = 0; i < response.body().data.size(); i++) {
                  orders.add(new orderHistory(response.body().data.get(i).at,
                      response.body().data.get(i).quantity,
                      response.body().data.get(i).status,
                      response.body().data.get(i).daily_meal));
                }
              }
              initializeAdapter();
            } else
              new MaterialDialog.Builder(context)
                  .content("مشکلی پیش آمده, دوباره تلاش کنید")
                  .contentGravity(GravityEnum.END)
                  .buttonsGravity(GravityEnum.START)
                  .cancelable(false).onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  dialog.cancel();
                }
              }).build().show();
          } catch (NullPointerException e) {
            new MaterialDialog.Builder(context)
                .content("مشکلی در سرور پیش آمده, لطفا بعدا امتحان کنید")
                .contentGravity(GravityEnum.END)
                .buttonsGravity(GravityEnum.START)
                .cancelable(false).onNeutral(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.cancel();
              }
            }).build().show();
          }
        }

        @Override
        public void onFailure(Call<History> call, Throwable t) {

        }
      });
  }

  private void initializeAdapter() {
    RVAdapter adapter = new RVAdapter(orders, context);
    rv.setAdapter(adapter);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    startActivity(new Intent(this, MainActivity.class));
  }
}