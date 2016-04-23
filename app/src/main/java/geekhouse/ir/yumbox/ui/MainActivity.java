package geekhouse.ir.yumbox.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.YumboxApp;
import geekhouse.ir.yumbox.data.api.ApiService;
import geekhouse.ir.yumbox.data.api.models.feed.feed;
import geekhouse.ir.yumbox.ui.adapters.MainActivityPagerAdapter;
import geekhouse.ir.yumbox.util.Constants;
import geekhouse.ir.yumbox.util.DailyMeals;
import geekhouse.ir.yumbox.util.Helper;
import geekhouse.ir.yumbox.util.ViewPagerCustomDuration;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPref;
    public static int numViews = 2;
    private Toolbar toolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int mSelectedId;
    private Context context = this;

    @Inject ApiService apiService;

    @Bind(R.id.pager) ViewPagerCustomDuration pager;
    ImageView ivCustomDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YumboxApp) getApplication()).getComponent().inject(this);
        isFirstLaunch();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        sharedPref = this.getPreferences(MODE_PRIVATE);

        setToolbar();
        initView();

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.enter_info,
                R.string.select_region);
        drawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawer.setItemIconTintList(null);
    }

    private void viewPagerDrag() {
        if (numViews > 1) {
            int firstTimer = sharedPref.getInt(Constants.IS_FIRST_TIME_INT, 0);
            if (firstTimer < 1) {
                firstTimer++;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Constants.IS_FIRST_TIME_INT, firstTimer);
                editor.apply();
                setViewPagerDrag();

            } else
                pager.userMotionSet(true);
        }
    }

    /**
     * if user swipes, cancel setCurrentItem.
     */
    @OnTouch(R.id.pager)
    @SuppressWarnings("unused")
    public boolean setViewPagerTouchListener(View v, MotionEvent event) {
        float x1 = 0, x2;
        final int MIN_DISTANCE = 75;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    pager.userMotionSet(true);
                    pager.setScrollDurationFactor(1);
                }
                break;
        }
        return false;
    }

    private void setPagerAdapter() {

        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager()));
        viewPagerDrag();
    }

    /**
     * goes to next page with 1/16 speed of default,
     * then goes back half-way
     */
    private void setViewPagerDrag() {
        pager.setScrollDurationFactor(16);
        final Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pager.setCurrentItem(1);
                        swipeTimer.cancel();
                    }
                });
            }
        }, 1200);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ivCustomDrawable = (ImageView) toolbar.findViewById(R.id.ivCustomDrawable);
        ivCustomDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END); //What ever your drawer gravity
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.gray_circle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("");
    }

    private void initView() {
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void itemSelection(int mSelectedId) {


        switch (mSelectedId) {

            case R.id.navigation_item_1:
                Toast.makeText(this, "what should i do here?", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_item_2:
                Intent dial = new Intent();
                dial.setAction("android.intent.action.DIAL");
                dial.setData(Uri.parse("tel:09361551123"));
                startActivity(dial);
                break;
            case R.id.navigation_item_3:
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("auth", MODE_PRIVATE);
                Constants.token = sharedPreferences.getString("authorization", "");
                startActivity(new Intent(context, OrderHistory.class));
                break;
            case R.id.navigation_item_4:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "whatIsIT?@telegramMe.com");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mSelectedId = menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //save selected item so it will remains same even after orientation change
        outState.putInt("SELECTED_ID", mSelectedId);
    }

    private void getFeed() {
        if (Helper.isConnected()) {
            final MaterialDialog materialDialog = Helper.newLoadingDialog(context);
            materialDialog.show();
            apiService.getFeed().enqueue(new retrofit2.Callback<feed>() {
                @Override
                public void onResponse(Call<feed> call, retrofit2.Response<feed> response) {
                    materialDialog.cancel();
                    try {
                        DailyMeals.dailyMeals = response.body().data;
                        numViews = DailyMeals.dailyMeals.size();
                        setPagerAdapter();
                    } catch (NullPointerException e) {
                        new MaterialDialog.Builder(context)
                                .content("مشکلی در سرور وجود دارد لطفا بعدا امتحان کنید.")
                                .cancelable(false);
                    }
                }

                @Override
                public void onFailure(Call<feed> call, Throwable t) {
                    materialDialog.cancel();
                    new MaterialDialog.Builder(context)
                            .content("مشکلی پیش آمده, دوباره تلاش کنید")
                            .contentGravity(GravityEnum.END)
                            .buttonsGravity(GravityEnum.START)
                            .cancelable(false).onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.cancel();
                            getFeed();
                        }
                    }).build().show();
                }
            });
        } else
            new MaterialDialog.Builder(context)
                    .content("گوشی شما با اینترنت متصل نمیباشد")
                    .contentGravity(GravityEnum.END)
                    .buttonsGravity(GravityEnum.START)
                    .cancelable(false)
                    .neutralText("تلاش دوباره")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.cancel();
                            getFeed();
                        }
                    }).build().show();
    }

    private void isFirstLaunch() {
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        if (sharedPref.getBoolean(Constants.IS_FIRST_TIME, true)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Constants.IS_FIRST_TIME, false);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else
            getFeed();

    }

}