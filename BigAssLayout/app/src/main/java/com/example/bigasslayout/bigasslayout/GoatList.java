package com.example.bigasslayout.bigasslayout;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by demo on 12/22/14.
 */


public class GoatList extends ArrayAdapter<String> {
    private final Activity context;
    private final String xmlSpeed;
    private boolean fibonacciBool;
    private final String[] goatNames;

    private final boolean[] goatTrue;
    private final int[] goatPix;


    private final boolean lotsOfObjects;

    View rowView;
    TextView rowTxt;
    ImageView rowImg;
    CheckBox rowCheck;
    int bignumber;
    int fibValue;



    boolean checked;
    CheckBox checkedBox;

    public GoatList (Activity context, String xmlspeed, Boolean fibonacciBool, String [] goatnames, int[] goatPix, boolean[] goatTrue, boolean lotsOfObjects){
        super(context, R.layout.goatrow, goatnames);
        this.context = context;
        this.xmlSpeed = xmlspeed;
        this.fibonacciBool = fibonacciBool;
        this.goatNames = goatnames;
        this.goatPix = goatPix;
        this.goatTrue = goatTrue;
        this.lotsOfObjects = lotsOfObjects;



    }
    @Override
        public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        if (xmlSpeed.matches(context.getResources().getString(R.string.slowestXml))) {
            //slow view is goatrow
            rowView= inflater.inflate(R.layout.goatrow, null, true);
        } else if (xmlSpeed == context.getResources().getString(R.string.overdrawXml)) {
            //use view with as much overdraw fremoved as possible
             rowView = inflater.inflate(R.layout.nooverdrawgoatrow, null, true);
        } else if (xmlSpeed == context.getResources().getString(R.string.removeLLoverdrawXml)) {
            //use view with as much overdraw fremoved as possible
            rowView = inflater.inflate(R.layout.nooverdrawgoatrow, null, true);
        } else if (xmlSpeed == context.getResources().getString(R.string.RLfastXml)) {
            //use view with as much overdraw removed as possible
            //this also removes one layer of the goat roaw by using a relative layout
            rowView = inflater.inflate(R.layout.rlfastestgoatrow, null, true);
        } else //fastest
        { rowView = inflater.inflate(R.layout.fastestgoatrow, null, true);
        }



            //add more objects - defaults to no
        if (lotsOfObjects == false) {
            // we'll use the views intialized once at the top.
            rowTxt = (TextView) rowView.findViewById(R.id.textView);
            rowImg = (ImageView) rowView.findViewById(R.id.imageView);
            rowCheck = (CheckBox) rowView.findViewById(R.id.checkBox);

            if (fibonacciBool == true) {
                //confusion and delay -take the position, add 4, multiply by another number to (a BIG number)
                //find that member of the fibonacci sequence using recursion (which is slow)
                if (position ==5){
                    bignumber  = (position+8)*3;//will cause a jink scrolling up
                }else if (position == 10) {
                    bignumber  = (position+3)*3;//will pause scrolling down
                }else{
                    bignumber = (position+4)*2;
                }
                fibValue = fibonacci.fib(bignumber);
                //wasted time!
                rowTxt.setText(goatNames[position] + "\nDelay Fibonacci #: " +fibValue);
            }
            else {//no crazy slowdown from fibbonaci.
                rowTxt.setText(goatNames[position]);
            }
           rowImg.setImageResource((goatPix[position]));
           rowCheck.setChecked(goatTrue[position]);
            rowCheck.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     checkedBox = (CheckBox) v;

                    //this all has to be inversed. If the user checks the box - it WAS unchecked
                    if (checkedBox.isChecked() ==false){
                        //button WAS checked. User unchecked it
                        //goat
                        checkedBox.setChecked(true);
                        Toast.makeText(context, "Correct! \nThis is a goat", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //button was not checked, and user checked it
                        //not a goat
                        checkedBox.setChecked(false);
                        Toast.makeText(context, "Come on, \nThis is a NOT a goat", Toast.LENGTH_SHORT).show();
                    }


                }
            });





        }
        else{ //lotsOfObjects is true
            //let's create new views every interation
            //hahahahah
            TextView rowTxtWaste = (TextView) rowView.findViewById(R.id.textView);
            ImageView rowImgWaste = (ImageView) rowView.findViewById(R.id.imageView);
            CheckBox rowCheckWaste = (CheckBox) rowView.findViewById(R.id.checkBox);
            //look a bunch of redundant objects created during rendering.  How silly :)
            if (fibonacciBool == true) {
                //confusion and delay -take the position, add 4, multiply by another number to (a BIG number)
                //find that member of the fibonacci sequence using recursion (which is slow)
                int bignumberWaste;
                if (position ==5){
                    bignumberWaste  = (position+8)*3;//will cause a jink scrolling up
                //}else if (position == 10) {
                  //  bignumberWaste  = (position+3)*3;//will pause scrolling down
                }else{
                    bignumberWaste = (position+4)*2;
                }
                int fibValueWaste;
                fibValueWaste = fibonacci.fib(bignumberWaste);
                //wasted time!
                rowTxtWaste.setText(goatNames[position] + "\nDelay Fibonacci #: " +fibValueWaste);
            }
            else {//no crazy slowdown.
                rowTxtWaste.setText(goatNames[position]);
            }

            rowImgWaste.setImageResource((goatPix[position]));
            rowCheckWaste.setChecked(goatTrue[position]);

            rowCheckWaste.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add a new variable for the checkbox
                    CheckBox checkedBoxWaste = (CheckBox) v;

                    //this all has to be inversed. If the user checks the box - it WAS unchecked
                    if (checkedBoxWaste.isChecked() ==false){
                        //button WAS checked. User unchecked it
                        //goat
                        checkedBoxWaste.setChecked(true);
                        Toast.makeText(context, "Correct! \nThis is a goat", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //button was not checked, and user checked it
                        //not a goat
                        checkedBoxWaste.setChecked(false);
                        Toast.makeText(context, "Come on, \nThis is a NOT a goat", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }



        return rowView;
    }

}

