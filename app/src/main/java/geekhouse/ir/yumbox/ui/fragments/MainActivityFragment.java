package geekhouse.ir.yumbox.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;
import de.hdodenhof.circleimageview.CircleImageView;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.YumboxApp;
import geekhouse.ir.yumbox.ui.OrderActivity;
import geekhouse.ir.yumbox.util.Constants;
import geekhouse.ir.yumbox.util.DailyMeals;
import geekhouse.ir.yumbox.util.Helper;

public class MainActivityFragment extends Fragment {

    public static final String TEST_PIC
            = "http://twistcatering.com/wp-content/uploads/2013/09/steak-bbq.jpg";

    int circleProgress = 100;
    int remainedFood;
    int i = 1;
    int viewNum;
    private static final int REP_DELAY = 100;
    private int price;
    private int remaining;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private Handler repeatUpdateHandler = new Handler();

    @Bind(R.id.price) TextView textViewPrice;
    @Bind(R.id.quantity) TextView textViewQuantity;
    @Bind(R.id.day) TextView dayTextView;
    @Bind(R.id.food_name) TextView foodNameTextView;
    @Bind(R.id.food_remaining) TextView textViewDown;

    @Bind(R.id.left_little_pic) CircleImageView leftImage;
    @Bind(R.id.middle_little_pic) CircleImageView middleImage;
    @Bind(R.id.right_little_pic) CircleImageView rightImage;
    @Bind(R.id.main_activity_main_course) CircleImageView mainCourse;

    @Bind(R.id.donut_progress) DonutProgress donutProgress;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance(int num) {

        MainActivityFragment viewPagerFragment = new MainActivityFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("num", num);

        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);

        viewNum = getArguments().getInt("num");

        ((YumboxApp) getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);
        setImages();

        price = DailyMeals.dailyMeals.get(viewNum).main_dish.price;
        remaining = DailyMeals.dailyMeals.get(viewNum).remained;
        setTextViews();
        setDay();
        foodNameTextView.setText(DailyMeals.dailyMeals.get(viewNum).main_dish.name);

        TypefaceHelper.typeface(view);
        return view;
    }

    private void setDay() {
        String at = DailyMeals.dailyMeals.get(viewNum).at;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        at = (at.substring(0, Math.min(at.length(), 10)));
        String comparedAgainst = df.format(new Date());
        if (at.equals(comparedAgainst))
            dayTextView.setText("امروز");
        else {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(df.parse(at));
                String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK), at);
                dayTextView.setText(dayOfWeek);
            } catch (ParseException e) {
                dayTextView.setText(at);
            }
        }
    }

    @OnClick(R.id.floating_action_button)
    @SuppressWarnings("unused")
    public void doneOnClickListener() {
        Constants.sendDay = dayTextView.getText().toString();
        Constants.numOrders = i;
        Constants.viewNum = viewNum;
        startActivity(new Intent(getActivity(), OrderActivity.class));
    }

    @OnClick(R.id.main_activity_main_course)
    @SuppressWarnings("unused")
    public void mainCourseClickListener() {
        if (Helper.isConnected())
            DetailFragment.newInstance(viewNum).show(getFragmentManager(), "detail");
        else {
            Helper.showNoInternetToast(getActivity());
        }
    }

    @OnClick(R.id.up_arrow_button)
    @SuppressWarnings("unused")
    public void upOnClickListener() {
        if (i < remaining) {
            i++;
            setTextViews();
        }
    }

    @OnLongClick(R.id.up_arrow_button)
    @SuppressWarnings("unused")
    public boolean upOnLongClickListener() {
        mAutoIncrement = true;
        repeatUpdateHandler.post(new RptUpdater());
        return false;
    }

    @OnTouch(R.id.up_arrow_button)
    @SuppressWarnings("unused")
    public boolean upOnTouchClickListener(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                && mAutoIncrement) {
            mAutoIncrement = false;
        }
        return false;
    }

    @OnClick(R.id.down_arrow_button)
    @SuppressWarnings("unused")
    public void downOnClickListener() {
        if (i >= 1) {
            i--;
            setTextViews();
        }
    }

    @OnLongClick(R.id.down_arrow_button)
    @SuppressWarnings("unused")
    public boolean downOnLongClickListener() {
        mAutoDecrement = true;
        repeatUpdateHandler.post(new RptUpdater());
        return false;
    }

    @OnTouch(R.id.down_arrow_button)
    @SuppressWarnings("unused")
    public boolean downOnTouchClickListener(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                && mAutoDecrement) {
            mAutoDecrement = false;
        }
        return false;
    }

    class RptUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                if (i < remaining) {
                    i++;
                    textViewPrice.setText(i * price + " تومان ");
                    textViewQuantity.setText(i + " پرس ");
                    repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
                }
            } else if (mAutoDecrement) {
                if (i >= 1) {
                    i--;
                    textViewPrice.setText(i * price + " تومان ");
                    textViewQuantity.setText(i + " پرس ");
                    repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
                }
            }
        }
    }

    class CircleIndicatorProgress implements Runnable {
        public void run() {
            circleProgress--;
            if (circleProgress > remainedFood) {
                donutProgress.setProgress(circleProgress);
                repeatUpdateHandler.postDelayed(new CircleIndicatorProgress(), 10);
            }
        }
    }

    private void setImages() {
        Picasso.with(getContext()).load(TEST_PIC).into(mainCourse, new Callback() {
            @Override
            public void onSuccess() {
                remainedFood = DailyMeals.dailyMeals.get(viewNum).remained * 100;
                remainedFood = remainedFood / DailyMeals.dailyMeals.get(viewNum).total;
                new CircleIndicatorProgress().run();
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(getContext()).load(TEST_PIC).into(leftImage);
        Picasso.with(getContext()).load(TEST_PIC).into(middleImage);
        Picasso.with(getContext()).load(TEST_PIC).into(rightImage);
    }

    private void setTextViews() {

        if (remaining > 20)
            textViewDown.setText(remaining + " پرس باقی مانده");
        else
            textViewDown.setText(" تنها " + remaining + " پرس باقی مانده");
        textViewPrice.setText(i * price + " تومان ");
        textViewQuantity.setText(i + " پرس ");
    }

    private String getDayOfWeek(int value, String defaultValue) {
        switch (value) {
            case 1:
                return "یکشنبه";
            case 2:
                return "دوشنبه";
            case 3:
                return "سه شنبه";
            case 4:
                return "چهارشنبه";
            case 5:
                return "پنجشنبه";
            case 6:
                return "جمعه";
            case 7:
                return "شنبه";
            default:
                return defaultValue;
        }
    }

}
