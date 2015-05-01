package com.sillars.imagescroll;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sillars.imagescroll.Analytics.TrackerName;

import com.sillars.imagescroll.MyScrollView.OnScrollStoppedListener;
//analytics
import com.newrelic.agent.android.NewRelic;
import com.crittercism.app.Crittercism;


public class MyActivity extends Activity {

    public int counter =0;
    RelativeLayout rl1;
    RelativeLayout rl2;
    MyScrollView sv;
    TextView hw;
    float swiptedTo;

    ImageView[] imageViews;
    //need initial scroll view height for comparisons later
    int InitialScrollViewHeight=30;
    int ScrollViewHeight=InitialScrollViewHeight;
    int imageheight = 500;
    int totalimagecount = 0;
    int ImagestoAdd = 10;
    Long responsetime;
    Long imagetime;
    Long RTT;
    Double AvgRTT;
    public static ArrayList<Integer> RTTtimes=  new ArrayList<Integer>();
    Tracker t;


    //network stuff
    ConnectivityManager cm;
    NetworkInfo activeNet;
    boolean isWiFi;
    String networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_my);
        rl1 = (RelativeLayout) findViewById(R.id.rl);
        hw = (TextView) findViewById(R.id.HelloWorld);
        sv=new MyScrollView(MyActivity.this, null);
        rl2 = new RelativeLayout(MyActivity.this);
        imageViews=new ImageView[100];
        sv.addView(rl2);
        rl1.addView(sv);


        //crittercism
        Crittercism.initialize(getApplicationContext(), "54fffdeeb59ef2d535335cd6");
        //new relic
        NewRelic.withApplicationToken(
                "AA9955a73e8163582b0f6040ed5cc0c39f4d798d11"
        ).start(this.getApplication());

        //Google analytics
        final GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        // Get tracker.
        //final Tracker t;
        t = analytics.newTracker(R.xml.app_tracker);
        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
        t.enableExceptionReporting(true);
        t.setScreenName("top of scroll");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        final Button dispatchButton = (Button) findViewById(R.id.timingDispatch);
        dispatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manually start a dispatch (Unnecessary if the tracker has a dispatch interval)
                analytics.dispatchLocalHits();
            }
        });




        //get screen height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenHeight = size.y;
        //could get width too if needed

        //add images
        totalimagecount = Imagelooper(ImagestoAdd, totalimagecount, rl2);
        //the height of the Scrollview has increased by the # of images added * the height of the images
        // ScrollViewHeight = ScrollViewHeight +ImagestoAdd*imageheight;


        sv.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                //call the class to figure out where we stop
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    sv.startScrollerTask();
                }

                return false;
            }});

        sv.setOnScrollStoppedListener(new OnScrollStoppedListener() {

            public void onScrollStopped() {
                //get location in scrollview after stop
                //getScrollY() is the top of the screen.. I want the bottom
                sv.getScrollY();
                int bottomofscreen = sv.getScrollY() + screenHeight + InitialScrollViewHeight;
                //how much scrollview is blwo the fold? How many images?
                int scrollBelowtheFold = ScrollViewHeight - bottomofscreen;
                //#images belwo the fold
                int ImagesBelowtheFold = Math.round(scrollBelowtheFold / imageheight);

                //Log.i("scrolling stopped", "stopped");
                Log.i("pixels below fold ", scrollBelowtheFold + " #images: " + ImagesBelowtheFold);


                //ok  now build in the logic to request more images.
                AvgRTT = RTTAverage();
                Log.i("averageRTT", AvgRTT.toString());
                //if we are dealing with *normal latency*
                hw.setText("RTT time: " + AvgRTT.toString());

                Log.i("pixels below fold ", scrollBelowtheFold + " #images: " + ImagesBelowtheFold);
                if (AvgRTT < 500) {
                    //latency is under 1second
                    ///begin download when #of images remaining is 1
                    //download default images to add
                    if (ImagesBelowtheFold < 2) {
                        totalimagecount = Imagelooper(ImagestoAdd, totalimagecount, rl2);


                    }

                }
                //medium latency
                else if (AvgRTT < 1500) {
                    //initiate download earlier
                    if (ImagesBelowtheFold < 5) {
                        //we could download more images here, but we don't
                        totalimagecount = Imagelooper(ImagestoAdd, totalimagecount, rl2);

                    }

                } else {
                    //latency is OVER 1.5s
                    //start earler and get more images  (2x image count)
                    //
                    if (ImagesBelowtheFold < 5) {
                        totalimagecount = Imagelooper(ImagestoAdd * 2, totalimagecount, rl2);

                    }

                }
                //get connection info
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNet = cm.getActiveNetworkInfo();
                isWiFi = activeNet.getType() == ConnectivityManager.TYPE_WIFI;
                if (isWiFi == true){
                    networkConnection = "Wi-Fi";
                }
                else { //cellular
                    TelephonyManager telMan = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    int networkType = telMan.getNetworkType();
                    switch (networkType)
                    {
                        case 7:
                            networkConnection="1xRTT";
                            break;
                        case 4:
                            networkConnection="CDMA";
                            break;
                        case 2:
                            networkConnection="EDGE";
                            break;
                        case 14:
                            networkConnection="eHRPD";
                            break;
                        case 5:
                            networkConnection="EVDO rev. 0";
                            break;
                        case 6:
                            networkConnection="EVDO rev. A";
                            break;
                        case 12:
                            networkConnection="EVDO rev. B";
                            break;
                        case 1:
                            networkConnection="GPRS";
                            break;
                        case 8:
                            networkConnection="HSDPA";
                            break;
                        case 10:
                            networkConnection="HSPA";
                            break;
                        case 15:
                            networkConnection="HSPA+";
                            break;
                        case 9:
                            networkConnection="HSUPA";
                            break;
                        case 11:
                            networkConnection="iDen";
                            break;
                        case 13:
                            networkConnection="LTE";
                            break;
                        case 3:
                            networkConnection="UMTS";
                            break;
                        case 0:
                            networkConnection="Unknown";
                            break;
                    }
                }
                Log.i("Network Type", networkConnection);
                hw.append("/n" + networkConnection);
                //metrics only on when there are 2 images below fold
                if (ImagesBelowtheFold < 2) {
                    //this is hacky, since it will not trigger for high latency (where new images are called wthen
                    //ImagesBelowtheFold <4.  But heck, its a sample app :)
                    //10 more images were just requested, so update the screen name
                    t.setScreenName(totalimagecount + " images");
                    t.send(new HitBuilders.ScreenViewBuilder()
                            .build());
                    Crittercism.leaveBreadcrumb(totalimagecount + " images");
                    t.send(new HitBuilders.TimingBuilder()
                            .setCategory("RTT").setValue(AvgRTT.longValue())
                            .setVariable("ImageRTT").setLabel("RTT").build());

                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("RTT Event")
                            .setValue(AvgRTT.longValue())
                            .setAction("ImageRTT").setLabel(networkConnection).build());
                    Crittercism.beginTransaction(networkConnection);
                    Crittercism.setTransactionValue(networkConnection, AvgRTT.intValue());
                    Crittercism.endTransaction(networkConnection);


                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public int Imagelooper(int numberofaddedimages, int TotalImageCount, RelativeLayout rl){


        for(int i=0;i<numberofaddedimages;i++)
        {
            //we are adding numberofaddedimages to the already populated list TotalImageCount
            //to correctly index this, add i to Total image count
            TotalImageCount = 1+TotalImageCount;
            //for analytics I want to track crashes.. so lets force it to crash
            //if we reach 100, the app crashes
            if(TotalImageCount ==99){
              //  TotalImageCount=0;
            }
            imageViews[TotalImageCount]=new ImageView(this);

            //    b[i]=new ImageView(this);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                    (int) ActionBar.LayoutParams.MATCH_PARENT,(int) ActionBar.LayoutParams.MATCH_PARENT);

            params.topMargin=ScrollViewHeight;
            //params.leftMargin=50;

            //  b[i].setText("Button "+i);
            //           counter =i;
            rl2.addView(imageViews[TotalImageCount]);
            Log.i("inserting view", "view #: "+ TotalImageCount);
            imageViews[TotalImageCount].setLayoutParams(params);
            new ImageDownloader().execute(TotalImageCount);
            ScrollViewHeight = ScrollViewHeight+imageheight;


        }



        return TotalImageCount;

    }



    public  Double RTTAverage() {
        //have RTT as Long
        //add it to the array
        //	RTTtimes.add(RTT);
        //average 5 values
        int arraylength = RTTtimes.size();

        int count =5;
        int sum = 0;
        //unless there are less than 5 in the array
        //do not use the average - return null and use teh default.

        if (arraylength>=count){

            for (int i=0; i< count; i++) {
                if (RTTtimes.get(arraylength -1 -i) != null){
                    sum = sum + RTTtimes.get(arraylength -1 -i);
                }
            }
        }

        double average = sum/count;

        return average;

    }




    private class ImageDownloader extends AsyncTask<Integer, Void,Bitmap> {


        @Override
        protected Bitmap doInBackground(Integer... param) {
            // TODO Auto-generated method stub

            return downloadBitmap(param[0]);
        }



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Bitmap result) {


            imageViews[counter].setImageBitmap(result);

        }

        private Bitmap downloadBitmap(Integer j) {
            //from interger counter, get url

            String url = Images.urls[j];
            counter =j;
            Log.i("image downloaded", "number: "+ j.toString());


            Long start = System.currentTimeMillis();
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();
                Long gotresponse = System.currentTimeMillis();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Long gotimage = System.currentTimeMillis();
                        responsetime  = gotresponse - start;
                        imagetime     = gotimage-start;
                        //since data is from same server assume just 2 RTT
                        RTT = responsetime/2;
                        RTTtimes.add(RTT.intValue());


                        Log.i("ImageDownloader", "image" + j +"responsetime (2RTT): "+ responsetime.toString());
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }







    }


}
