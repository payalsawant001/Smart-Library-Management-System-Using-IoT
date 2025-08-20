package com.akash.booklibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkState {


    public static boolean isInternetAvailable(Context context) {
        boolean result = false;
        ConnectivityManager check = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check != null) {
            NetworkInfo.State mobile = check.getNetworkInfo(0).getState();
            //wifi
            NetworkInfo.State wifi = check.getNetworkInfo(1).getState();
            if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                result = true;
            }
        }

        return result;

        /*InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(1, TimeUnit.MINUTES);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");*/
    }
}
