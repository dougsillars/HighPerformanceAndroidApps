package com.example.bigasslayout.bigasslayout;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

public class BALayout extends Activity {


    //views
    View mainView;
    GoatList adapter;
    TextView textTotal;
    RadioButton xmlChoice;
    //options menu id
    int id;

    //parameters for views
    //default use slowest XML
    String xmlUsed;
    //by default use the fibonacci delay off
    boolean useFibonacci = false;
    String useFib;
    //by default invalidate the main view
    boolean invaliatdeMainView = true;
    String mainViewStat;
    //adding objects during rendering
    boolean lotsOfObjects = false;
    String didIAddObjects;
    boolean memoryLeakTF = false;
    String MemoryLeakText;


    //variables

    static String[] goatNames;
    static int[] goatPix;
    static boolean[] goatTrue;


    //timer stuff
    long starttime;
    long finnish;
    long totalTime;
    String timeString = "foo";

    //save display variables for screen rotation
    static final String STATE_XML = "XMl file used";
    static final String STATE_fibb = "Fib used?";
    static final String STATE_ivValdateViews = "Views Invalidated?";
    static final String STATE_AddedObjects = "ADded Extra Objects?";
    static final String STATE_Leak = "Leaky?";
    static final String STATE_GoatPicName = "goat pic text";
    static final String STATE_GoatPix = "Goat Pix";
    static final String STATE_GoatTF = "is it a goat";


    //This is creating a memory leak
    static ArrayList<byte[]> iceSheet = new ArrayList<byte[]>();
    static Iceberg iceberg = null;
    static byte[] mostlyUnderwater;
    class Iceberg{
        void sink(){
            mostlyUnderwater = new byte[2048 * 1024];
            iceSheet.add(mostlyUnderwater);//icesheet should grow by 2MB every rotation
            Log.i("iceberg", "Captain, I think we might have hit something.");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_XML, xmlUsed);
        savedInstanceState.putBoolean(STATE_fibb, useFibonacci);
        savedInstanceState.putBoolean(STATE_ivValdateViews, invaliatdeMainView);
        savedInstanceState.putBoolean(STATE_AddedObjects, lotsOfObjects);
        savedInstanceState.putBoolean(STATE_Leak, memoryLeakTF);
        savedInstanceState.putStringArray(STATE_GoatPicName, goatNames);
        savedInstanceState.putBooleanArray(STATE_GoatTF, goatTrue);
        savedInstanceState.putIntArray(STATE_GoatPix, goatPix);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //default attribute values
        xmlUsed = getResources().getString(R.string.slowestXml);
        useFib = getResources().getString(R.string.fibbofalse);
        mainViewStat = getResources().getString(R.string.mainviewInvalidatedtrue);
        didIAddObjects = getResources().getString(R.string.extraObjectsfalse);
        MemoryLeakText = getResources().getString(R.string.memoryLeakfalse);



        //load in the data from previous view - or use default data
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            xmlUsed = savedInstanceState.getString(STATE_XML);
            useFibonacci = savedInstanceState.getBoolean(STATE_fibb);
            invaliatdeMainView = savedInstanceState.getBoolean(STATE_ivValdateViews);
            lotsOfObjects = savedInstanceState.getBoolean(STATE_AddedObjects);
            memoryLeakTF = savedInstanceState.getBoolean(STATE_Leak);
         if (lotsOfObjects == false) {
             //load from saved state
             goatNames = savedInstanceState.getStringArray(STATE_GoatPicName);
             goatPix = savedInstanceState.getIntArray(STATE_GoatPix);
             goatTrue = savedInstanceState.getBooleanArray(STATE_GoatTF);
         } else {
             //more stuff! Bwa ha ha ha
             goatNames = new String[]{
                     "http://bit.ly/1JOpkQo",
                     "http://bit.ly/1ADUYfk",
                     "http://bit.ly/1t701Eh",
                     "http://bit.ly/1xdCS4D",
                     "http://bit.ly/1B1UVb8",
                     "rooster",
                     "Ani the Pygora",
                     "Llama",
                     "Alden the Pygora",
                     "http://bit.ly/16NeqeA",
                     "http://bit.ly/1zfqTym",
                     "http://bit.ly/1AWSOpf",
                     "http://bit.ly/1sUlPNI"

             };
             goatPix = new int[]{
                     R.drawable.babygoatsjan2007crop,
                     R.drawable.mountaingoat,
                     R.drawable.goatwithunusualhorns,
                     R.drawable.hausziege04,
                     R.drawable.feralgoat,
                     R.drawable.rooster,
                     R.drawable.ani,
                     R.drawable.llama,
                     R.drawable.alden,
                     R.drawable.goatracing,
                     R.drawable.goatinacar,
                     R.drawable.boogoat,
                     R.drawable.donkey

             };

             goatTrue = new boolean[]{
                     true,
                     true,
                     true,
                     true,
                     true,
                     false,
                     true,
                     false,
                     true,
                     true,
                     true,
                     true,
                     false

             };
         }
        }
        else {
            //initialize
            goatNames = new String[]{
                    "http://bit.ly/1JOpkQo",
                    "http://bit.ly/1ADUYfk",
                    "http://bit.ly/1t701Eh",
                    "http://bit.ly/1xdCS4D",
                    "http://bit.ly/1B1UVb8",
                    "rooster",
                    "Ani the Pygora",
                    "Llama",
                    "Alden the Pygora",
                    "http://bit.ly/16NeqeA",
                    "http://bit.ly/1zfqTym",
                    "http://bit.ly/1AWSOpf",
                    "http://bit.ly/1sUlPNI"

            };
            goatPix = new int[]{
                    R.drawable.babygoatsjan2007crop,
                    R.drawable.mountaingoat,
                    R.drawable.goatwithunusualhorns,
                    R.drawable.hausziege04,
                    R.drawable.feralgoat,
                    R.drawable.rooster,
                    R.drawable.ani,
                    R.drawable.llama,
                    R.drawable.alden,
                    R.drawable.goatracing,
                    R.drawable.goatinacar,
                    R.drawable.boogoat,
                    R.drawable.donkey

            };

            goatTrue = new boolean[]{
                    true,
                    true,
                    true,
                    true,
                    true,
                    false,
                    true,
                    false,
                    true,
                    true,
                    true,
                    true,
                    false

            };
        }
        if (memoryLeakTF ==true) {
            //calling the memory leak class
            iceberg = new Iceberg();
            iceberg.sink();
        }
        //draw the views. This will vary depending on the settings
        createTheViews(xmlUsed, useFibonacci);

    }
    @Override
    public void onDestroy() {
        //only destroy stuff if expictly say so :)
        if (invaliatdeMainView == true) {
            mainView = null;
            textTotal = null;
           adapter = null;
            goatPix = null;
            goatNames = null;
            goatTrue = null;

        }
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.balayout, menu);


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //fix the menu options on screen rotation.

        if (xmlUsed == getResources().getString(R.string.slowestXml)) {
            menu.getItem(1).setChecked(true);
        } else if (xmlUsed == getResources().getString(R.string.overdrawXml)) {
            menu.getItem(2).setChecked(true);
        } else if (xmlUsed == getResources().getString(R.string.removeLLoverdrawXml)) {
            menu.getItem(3).setChecked(true);
        } else if (xmlUsed == getResources().getString(R.string.fastXml)) {
            menu.getItem(4).setChecked(true);
        } else if (xmlUsed == getResources().getString(R.string.RLfastXml)) {
            menu.getItem(5).setChecked(true);
        } else {
            //default is slowest
            menu.getItem(1).setChecked(true);
        }
        //now fix fibonacci, invaldations and creating objects
        if (useFibonacci == true) {
            menu.getItem(6).setChecked(true);//set fib on
        } else {
            menu.getItem(6).setChecked(false);
        }//set false
        if (invaliatdeMainView == true) {
            menu.getItem(7).setChecked(true);//set ivalidate on
        } else {
            menu.getItem(7).setChecked(false);
        }//set false
        if (lotsOfObjects == true) {
        menu.getItem(8).setChecked(true);//set create obj on
        } else {
            menu.getItem(8).setChecked(false);
        }//set false
        if (memoryLeakTF == true) {
            menu.getItem(9).setChecked(true);//set create obj on
        } else {
            menu.getItem(9).setChecked(false);
        }//set false

        super.onPrepareOptionsMenu(menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is

        switch (item.getItemId()) {
            case R.id.slowest:
                xmlUsed = getResources().getString(R.string.slowestXml);
                item.setChecked(true);
                break;
            case R.id.removeOverDraw:
                xmlUsed = getResources().getString(R.string.overdrawXml);
                item.setChecked(true);
                break;
            case R.id.RemoveLLremoveOverDraw:
                xmlUsed = getResources().getString(R.string.removeLLoverdrawXml);
                item.setChecked(true);
                break;
            case R.id.fast:
                xmlUsed = getResources().getString(R.string.fastXml);
                item.setChecked(true);
                break;
            case R.id.fastRLinrow:
                xmlUsed = getResources().getString(R.string.RLfastXml);
                item.setChecked(true);
                break;
            case R.id.useFib:
                if (item.isChecked()) {
                    item.setChecked(false);
                    useFibonacci = false;
                    useFib = getResources().getString(R.string.fibbofalse);
                } else {
                    item.setChecked(true);
                    useFibonacci = true;
                    useFib = getResources().getString(R.string.fibbotrue);
                }
                break;
            case R.id.invalidateMain:
                if (item.isChecked()) {
                    item.setChecked(false);
                    invaliatdeMainView = false;
                    mainViewStat = getResources().getString(R.string.mainviewInvalidatedfalse);
                } else {
                    item.setChecked(true);
                    invaliatdeMainView = true;
                    mainViewStat = getResources().getString(R.string.mainviewInvalidatedtrue);
                    ;
                }
                break;
            case R.id.addObjects:
                if (item.isChecked()) {
                    item.setChecked(false);
                    lotsOfObjects = false;
                    didIAddObjects = getResources().getString(R.string.extraObjectsfalse);
                } else {
                    item.setChecked(true);
                    lotsOfObjects = true;
                    didIAddObjects = getResources().getString(R.string.extraObjectstrue);
                }
                break;
            case R.id.memoryLeak:
                if (item.isChecked()) {
                    item.setChecked(false);
                    memoryLeakTF = false;
                    MemoryLeakText = getResources().getString(R.string.memoryLeakfalse);
                } else {
                    item.setChecked(true);
                    memoryLeakTF = true;
                    MemoryLeakText = getResources().getString(R.string.memoryLeaktrue);
                }
                break;
        }

        if (invaliatdeMainView == true) {
           mainView.invalidate();
            adapter.notifyDataSetInvalidated();
            if (adapter.rowCheck != null) {
                adapter.rowCheck.invalidate();
            }
        }
        //else   - dont. :)

        createTheViews(xmlUsed, useFibonacci);


    }

    public void createTheViews(String xml, Boolean fib) {


        starttime = System.currentTimeMillis();
        //the main layout can become more optimized.

        if (xml == getResources().getString(R.string.slowestXml)) {
            setContentView(R.layout.activity_balayout);
            mainView = findViewById(R.id.bloadtedLayout);
        } else if (xml == getResources().getString(R.string.overdrawXml)) {
            setContentView(R.layout.nooverdrawactivity_balayout);
            mainView = findViewById(R.id.noOverdrawLayout);
        } else if (xml == getResources().getString(R.string.removeLLoverdrawXml)) {
            setContentView(R.layout.nollnooverdrawactivity_balayout);
            mainView = findViewById(R.id.noLLnoOverdrawLayout);
        } else {
            //fastestxml //this will also giveus the RL optimized version, since that is in the Goatrow XML.
            setContentView(R.layout.fastestactivity_balayout);
            mainView = findViewById(R.id.fastestLayout);


        }


        ListView list;

        //the goatlist can be optimized by modifying the parameters
        adapter = new GoatList(BALayout.this, xmlUsed, useFibonacci, goatNames, goatPix, goatTrue, lotsOfObjects);

        list = (ListView) findViewById(R.id.listofgoats);

        list.setAdapter(adapter);


        if (lotsOfObjects == true) {

            //this is a clicklistener I was playing with, and forgot about and left in. oops.
            //when say create more objects, I'll toss this bad boy back into the mix :)

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(BALayout.this, "You Clicked on " + goatNames[+position], Toast.LENGTH_SHORT).show();
                }
            });
        }

        textTotal = (TextView) findViewById(R.id.textViewTotal);
        //how long did this take?
        //get time
        //textTotal = (TextView) findViewById(R.id.textViewTotal);
        finnish = System.currentTimeMillis();
        totalTime = finnish - starttime;
        timeString = String.valueOf(totalTime);


        textTotal.append(timeString);
        textTotal.append("\n" + xmlUsed + ", " + useFib + ", " + mainViewStat + ", " + didIAddObjects+", " + MemoryLeakText);
    }


}


