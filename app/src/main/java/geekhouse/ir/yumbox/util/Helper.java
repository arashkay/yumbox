package geekhouse.ir.yumbox.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Copyright (c) 2015-2016 www.Tipi.me.
 * Created by Ashkan Hesaraki.
 * Ashkan@tipi.me
 */
public class Helper {

    static ConnectivityManager cm;
    public static Context context;

    public static void showNoInternetToast(Context context) {

        Toast.makeText(context, "گوشی شما به اینترنت وصل نمی باشد", Toast.LENGTH_SHORT).show();
    }

    public static MaterialDialog newLoadingDialog(Context context) {

        return new MaterialDialog.Builder(context)
                .content("Loading ...")
                .progress(true, 0)
                .cancelable(false).build();
    }

    public static boolean isConnected() {
        if (cm == null)
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }
}
