package geekhouse.ir.yumbox;

import android.app.Application;
import android.graphics.Typeface;


import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

import geekhouse.ir.yumbox.data.DataModule;
import geekhouse.ir.yumbox.data.api.ApiModule;
import geekhouse.ir.yumbox.util.Helper;
import timber.log.Timber;

public class YumboxApp extends Application {

    MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        mainComponent = DaggerMainComponent.builder()
                .yumboxModule(new YumboxModule(this))
                .apiModule(new ApiModule())
                .dataModule(new DataModule()).build();

        Helper.context = this;

        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/BK.TTF"))
                .create();
        TypefaceHelper.init(typeface);
    }

    public MainComponent getComponent() {
        return mainComponent;
    }
}
