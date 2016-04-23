package geekhouse.ir.yumbox.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.ui.adapters.IntroPagerAdapter;
import geekhouse.ir.yumbox.util.ParallaxViewPager;

public class IntroActivity extends FragmentActivity {

  @Bind(R.id.launch_view_pager) ParallaxViewPager pager;
  @Bind(R.id.yum_box_launch) TextView textView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.intro_activity);

    ButterKnife.bind(this);

    setPagerAdapter();

    Typeface blockFonts = Typeface.createFromAsset(getAssets(),"fonts/Bauhaus_93.ttf");

    TypefaceHelper.typeface(this);

    textView.setTypeface(blockFonts);
  }

  private void setPagerAdapter(){

    pager.setOffscreenPageLimit(2);
    pager.setOverlapPercentage(0.85f);
    pager.setScaleType(ParallaxViewPager.FIT_HEIGHT);
    pager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));
    setViewPagerDrag();
  }

  /**
   * if user swipes, cancel setCurrentItem.
   */
  @OnTouch(R.id.launch_view_pager)
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


}
