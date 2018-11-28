package com.prouner.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.prouner.main.MainMVP;

public class ConnectivityInfoReceiver extends BroadcastReceiver {

    public static final String TAG = ConnectivityInfoReceiver.class.getSimpleName();

    private MainMVP.Presenter mPresenter;
    private Context mContext;

    public ConnectivityInfoReceiver(MainMVP.Presenter presenter, Context context){
        mPresenter = presenter;
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logInfo(intent);
        if(!intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)){
            Log.v(TAG, "Connection is back");
            mPresenter.onNetworkConnectionRestored(mContext, this);
        }
    }


    private void logInfo(Intent intent) {
        Log.v(TAG, "action: " + intent.getAction());
        Log.v(TAG, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.v(TAG, "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.v(TAG, "no extras");
        }
    }
}
