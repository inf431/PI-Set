package com.example.damien.pi_set;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Collections;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {
    private LinkedList<Integer> deck;
    public int DECK_SIZE=81;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table=(TableLayout)findViewById(R.id.tableLayout1);

        // Création et mélange du paquet de cartes
        deck=new LinkedList<Integer>();
        for(int i=0;i<DECK_SIZE;i++){
            deck.add(Cards.correctRange(i));
        }
        Collections.shuffle(deck);

        // Ajout de lignes dans lesquels on affiche les cartes
        TableLayout.LayoutParams param = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
       for(int i=0;i<3;i++){
           TableRow row=new TableRow(getApplicationContext());
           row.setWeightSum(4.0f);
           row.setBackgroundColor(Color.BLUE);
           row.setLayoutParams(param);
           table.addView(row);
       }

        //Ajout de cartes aux lignes
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,1.0f);


        for(int i=0;i<3;i++){

            TableRow row=(TableRow) table.getChildAt(i);
            for(int j=1;j<5;j++){
            ImageButton Card=new ImageButton(getApplicationContext());
            Card.setImageDrawable(new CardDrawable(deck.pop()));
            Card.setLayoutParams(param2);
            row.addView(Card);
            }
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
