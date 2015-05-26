package com.example.bigasslayout.bigasslayout;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by demo on 5/24/15.
 */
//added an application class to install Leak Canary

public class AmiAGoat extends Application {

    //LeakCanary reference watcher
    public static RefWatcher getRefWatcher(Context context) {
        AmiAGoat application = (AmiAGoat) context.getApplicationContext();
        return application.refWatcher;
    }
    private RefWatcher refWatcher;


    @Override public void onCreate() {
        super.onCreate();
        //on app creation - turn on leakcanary

        refWatcher = LeakCanary.install(this);
    }

}
