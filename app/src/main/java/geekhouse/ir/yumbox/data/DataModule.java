package geekhouse.ir.yumbox.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by A on 1/16/2016.
 */
@Module
public class DataModule {


    @Provides
    @Singleton
    SharedPreferences provideSharedPreferencesToken(Application application) {
        return application.getSharedPreferences("Auth", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application application) {
        return new Picasso.Builder(application)
//                .downloader(new OkHttpDownloader(client))
                .listener(new Picasso.Listener() {
                    @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                        Timber.e(e, "Failed to load image: %s", uri);
                    }
                })
                .build();
    }

}
