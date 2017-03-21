package tk.mrinalini.cureme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class Utils {

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
