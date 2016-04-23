package geekhouse.ir.yumbox.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.YumboxApp;
import geekhouse.ir.yumbox.data.api.ApiService;
import geekhouse.ir.yumbox.data.api.models.Customer.Customer;
import geekhouse.ir.yumbox.data.api.models.Success;
import geekhouse.ir.yumbox.ui.fragments.MainActivityFragment;
import geekhouse.ir.yumbox.util.Constants;
import geekhouse.ir.yumbox.util.DailyMeals;
import geekhouse.ir.yumbox.util.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private final Context context = this;

    @Bind(R.id.address) EditText address;
    @Bind(R.id.name) EditText name;
    @Bind(R.id.cellphone) EditText cellphone;

    @Bind(R.id.number_of_orders) TextView numOrders;
    @Bind(R.id.price) TextView price;
    @Bind(R.id.send_day) TextView sendDay;

    @Bind(R.id.main_course) CircleImageView mainCourse;
    @Bind(R.id.left_little_pic) CircleImageView leftPic;
    @Bind(R.id.middle_little_pic) CircleImageView middlePic;
    @Bind(R.id.right_little_pic) CircleImageView rightPic;

    @Bind(R.id.region) AutoCompleteTextView region;

    @Bind(R.id.order_page_floating_action_button) FloatingActionButton done;

    @Inject ApiService apiService;
    static SharedPreferences sharedPreferences;

    final String PERSIAN_WORDS = "^[\\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        ButterKnife.bind(this);
        ((YumboxApp) getApplication()).getComponent().inject(this);
        setRegionAdapter();
        TypefaceHelper.typeface(this);
        setImagesAndText();
    }

    @OnClick(R.id.order_page_floating_action_button)
    @SuppressWarnings("unused")
    public void doneClickListener() {
        sharedPreferences = getApplication().getSharedPreferences("auth", MODE_PRIVATE);
        String token = sharedPreferences.getString("authorization", "");
        if (!token.isEmpty())
            Constants.token = token;
        if (token.isEmpty())
            token = null;
        if (Helper.isConnected()) {
            if (editTextValidation()) {
                apiService.signUp(Constants.numOrders,
                        DailyMeals.dailyMeals.get(Constants.viewNum).doc_key,
                        new Customer(cellphone.getText().toString(),
                                address.getText().toString(), name.getText().toString()),
                        token)
                        .enqueue(new Callback<Success>() {
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                try {
                                    if (response.body().success) {
                                        setTokenIfFirstTime(response);
                                        startActivity(new Intent(context, OrderHistory.class));
                                    }
                                } catch (NullPointerException e) {
                                    new MaterialDialog.Builder(context)
                                            .content("مشکلی پیش آمده, دوباره تلاش کنید")
                                            .contentGravity(GravityEnum.END)
                                            .buttonsGravity(GravityEnum.START)
                                            .cancelable(false).onNeutral(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.cancel();
                                            done.performClick();
                                        }
                                    }).build().show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                new MaterialDialog.Builder(context)
                                        .content("متاسفانه مشکلی پیش آمده")
                                        .cancelable(true)
                                        .build().show();
                            }
                        });
            }
        } else {
            Helper.showNoInternetToast(context);
        }
    }

    /**
     * set strings for autoComplete
     */
    private void setRegionAdapter() {

        String[] regions = getResources().getStringArray(R.array.list_of_regions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        region.setAdapter(adapter);
    }

    private void setImagesAndText() {
        setImages();
        price.setText(DailyMeals.dailyMeals.get(Constants.viewNum).main_dish.price * Constants.numOrders + " تومان");
        numOrders.setText(Constants.numOrders + " پرس");
        sendDay.setText(Constants.sendDay);
    }

    private void setImages() {
        Picasso.with(this).load(MainActivityFragment.TEST_PIC).into(mainCourse);
        Picasso.with(this).load(MainActivityFragment.TEST_PIC).into(leftPic);
        Picasso.with(this).load(MainActivityFragment.TEST_PIC).into(middlePic);
        Picasso.with(this).load(MainActivityFragment.TEST_PIC).into(rightPic);
    }

    private boolean editTextValidation() {

        if (region.getText().toString().trim().length() == 0 ) {
            region.setError(getResources().getString(R.string.region_required_hint_empty));
            region.requestFocus();
            return false;
        }

        if (region.getText().toString().contains(PERSIAN_WORDS)) {

            region.setError(getResources().getString(R.string.region_required_hint_wrong));
            region.requestFocus();
            return false;
        }

        if (address.getText().toString().trim().length() == 0) {
            address.setError(getResources().getString(R.string.region_required_hint_empty));
            address.requestFocus();
            return false;
        }

        if (address.getText().toString().contains(PERSIAN_WORDS)) {

            address.setError(getResources().getString(R.string.address_required_hint_wrong));
            address.requestFocus();
            return false;
        }

        if (name.getText().toString().trim().length() == 0) {
            name.setError(getResources().getString(R.string.name_required_hint_empty));
            name.requestFocus();
            return false;
        }


        if (name.getText().toString().contains(PERSIAN_WORDS)) {

            name.setError(getResources().getString(R.string.name_required_hint_wrong));
            name.requestFocus();
            return false;
        }

        if (!cellphone.getText().toString().startsWith("09") || cellphone.getText().toString().length() != 11) {

            cellphone.setError(getResources().getString(R.string.cell_phone_wrong_hint));
            cellphone.requestFocus();
            return false;
        }

        return true;
    }

    private void setTokenIfFirstTime(Response response) {
        if (sharedPreferences.getString("authorization", "").isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("authorization", response.headers().get("authorization"));
            Constants.token = response.headers().get("authorization");
            editor.apply();
        }
    }

}