package com.sillars.imagescroll;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by demo on 3/10/15.
 */
public class Analytics extends Application {

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-60495201-1";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
      //  GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
       // ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public Analytics() {
        super();
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
               Tracker t =  analytics.newTracker(R.xml.app_tracker);
                t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }


}