package geekhouse.ir.yumbox.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

import timber.log.Timber;

@SuppressLint("NewApi")
public class ParallaxViewPager extends ViewPager {

  public static final int FIT_WIDTH = 0;
  public static final int FIT_HEIGHT = 1;
  public static final float OVERLAP_FULL = 1f;
  public static final float OVERLAP_HALF = 0.5f;
  public static final float OVERLAP_QUARTER = 0.25f;
  public PageChangeParallaxListener listener;
  private static final float CORRECTION_PERCENTAGE = 0.01f;
  public Bitmap bitmap;
  private Rect source, destination;
  private int scaleType;
  private int chunkWidth;
  private int projectedWidth;
  private float overlap;
  private OnPageChangeListener secondOnPageChangeListener;

  public ParallaxViewPager(Context context) {
    super(context);
    init();
    postInitViewPager();
  }

  public ParallaxViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
    postInitViewPager();
  }

  private void init() {
    source = new Rect();
    destination = new Rect();
    scaleType = FIT_HEIGHT;
    overlap = OVERLAP_HALF;
    listener = new PageChangeParallaxListener();
    setOnPageChangeListener(listener);

    setOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (bitmap != null) {
          Timber.d(String.valueOf(position) + "/" + String.valueOf(positionOffset) + "/" + String.valueOf(positionOffsetPixels) + "/");
          source.left = (int) Math.floor((position + positionOffset - CORRECTION_PERCENTAGE) * chunkWidth);
          source.right = (int) Math.ceil((position + positionOffset + CORRECTION_PERCENTAGE) * chunkWidth + projectedWidth);
          destination.left = (int) Math.floor((position + positionOffset - CORRECTION_PERCENTAGE) * getWidth());
          destination.right = (int) Math.ceil((position + positionOffset + 1 + CORRECTION_PERCENTAGE) * getWidth());
          invalidate();
        }

        if (secondOnPageChangeListener != null) {
          secondOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
      }

      @Override
      public void onPageSelected(int position) {
        if (secondOnPageChangeListener != null) {
          secondOnPageChangeListener.onPageSelected(position);
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        if (secondOnPageChangeListener != null) {
          secondOnPageChangeListener.onPageScrollStateChanged(state);
        }
      }
    });
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    destination.top = 0;
    destination.bottom = h;
    if (getAdapter() != null && bitmap != null)
      calculateParallaxParameters();
  }

  private void calculateParallaxParameters() {
    if (bitmap.getWidth() < getWidth() && bitmap.getWidth() < bitmap.getHeight() && scaleType == FIT_HEIGHT) {
      Log.w(ParallaxViewPager.class.getName(), "Invalid bitmap bounds for the current device, parallax effect will not work.");
    }

    final float ratio = (float) getHeight() / bitmap.getHeight();
    if (ratio != 1) {
      switch (scaleType) {
        case FIT_WIDTH:
          source.top = (int) ((bitmap.getHeight() - bitmap.getHeight() / ratio) / 2);
          source.bottom = bitmap.getHeight() - source.top;
          chunkWidth = (int) Math.ceil((float) bitmap.getWidth() / (float) getAdapter().getCount());
          projectedWidth = chunkWidth;
          break;
        case FIT_HEIGHT:
        default:
          source.top = 0;
          source.bottom = bitmap.getHeight();
          projectedWidth = (int) Math.ceil(getWidth() / ratio);
          chunkWidth = (int) Math.ceil((bitmap.getWidth() - (projectedWidth / 3)) / (float) getAdapter().getCount() * overlap);
          break;
      }
    }
  }

  @Override public void setBackgroundResource(int resid) {
    bitmap = BitmapFactory.decodeResource(getResources(), resid);
  }

  @Override public void setBackground(Drawable background) {
    bitmap = ((BitmapDrawable) background).getBitmap();
  }

  @Override public void setBackgroundDrawable(Drawable background) {
    bitmap = ((BitmapDrawable) background).getBitmap();
  }

  public ParallaxViewPager setBackground(Bitmap bitmap) {
    this.bitmap = bitmap;
    return this;
  }

  public ParallaxViewPager setScaleType(final int scaleType) {
    if (scaleType != FIT_WIDTH && scaleType != FIT_HEIGHT)
      throw new IllegalArgumentException("Illegal argument: scaleType must be FIT_WIDTH or FIT_HEIGHT");
    this.scaleType = scaleType;
    return this;
  }

  public ParallaxViewPager setOverlapPercentage(final float percentage) {
    if (percentage <= 0 || percentage > 1)
      throw new IllegalArgumentException("Illegal argument: percentage must be between 0 and 1");
    overlap = percentage;
    return this;
  }

  public ParallaxViewPager invalidateParallaxParameters() {
    calculateParallaxParameters();
    return this;
  }

  @Override protected void onDraw(Canvas canvas) {
    if (bitmap != null)
      canvas.drawBitmap(bitmap, source, destination, null);
  }

  public void addOnPageChangeListener(OnPageChangeListener listener) {
    secondOnPageChangeListener = listener;
  }

  public ViewPager.OnPageChangeListener getPageChangeListener() {
    return listener;
  }

  public class PageChangeParallaxListener implements
      ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
      if (bitmap != null) {
        source.left = (int) Math
            .floor((position + positionOffset - CORRECTION_PERCENTAGE)
                * chunkWidth);
        source.right = (int) Math
            .ceil((position + positionOffset + CORRECTION_PERCENTAGE)
                * chunkWidth + projectedWidth);
        destination.left = (int) Math
            .floor((position + positionOffset - CORRECTION_PERCENTAGE)
                * getWidth());
        destination.right = (int) Math.ceil((position + positionOffset
            + 1 + CORRECTION_PERCENTAGE)
            * getWidth());
        invalidate();
      }

      if (secondOnPageChangeListener != null) {
        secondOnPageChangeListener.onPageScrolled(position,
            positionOffset, positionOffsetPixels);
      }
    }

    @Override
    public void onPageSelected(int position) {
      if (secondOnPageChangeListener != null) {
        secondOnPageChangeListener.onPageSelected(position);
      }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
      if (secondOnPageChangeListener != null) {
        secondOnPageChangeListener.onPageScrollStateChanged(state);
      }
    }

  }

  static private Boolean check = false;
  static int i = 0;

  public void userMotionSet(Boolean result){
    check = result;
  }

  private ScrollerCustomDuration mScroller = null;

  private void postInitViewPager() {
    try {
      Field scroller = ViewPager.class.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
      interpolator.setAccessible(true);

      mScroller = new ScrollerCustomDuration(getContext(),
          (Interpolator) interpolator.get(null));
      scroller.set(this, mScroller);
    } catch (Exception e) {
    }
  }


  public void setScrollDurationFactor(double scrollFactor) {
    mScroller.setScrollDurationFactor(scrollFactor);
  }

  @Override
  protected void onPageScrolled(int position, float offset, int offsetPixels) {
    super.onPageScrolled(position, offset, offsetPixels);
    if(i == 0 && !check) {
      if (position == 0) {
        if (offset > 0.5) {
          i++;
          setCurrentItem(0);
        }

      }
    }
  }
}
