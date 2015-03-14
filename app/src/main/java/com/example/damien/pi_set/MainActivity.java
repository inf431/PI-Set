package com.example.damien.pi_set;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View.OnClickListener;

import java.util.Collections;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {
    public int DECK_SIZE=81;
    private LinkedList<Integer> deck;

    //Deux champs pour mémoriser la sélection du joueur, ainsi que le nombre de cartes sélectionnées
    //Le cas selection[i]=-1 correspond au cas où moins de 3 cartes sont sélectionnées
    private int[] selection = new int[3];
    private int numberOfSelectedCards=0;


    //Gère la sélection d'une carte par le joueur
    private OnClickListener selectedListener = new OnClickListener(){
        public void onClick(View v){


            ImageButton button = (ImageButton) v;
            CardDrawable card = (CardDrawable) button.getDrawable();
            int CardId = card.getCard();

            if (!card.isSelected()&&numberOfSelectedCards<3) {
                card.select();
                button.setBackgroundColor(Color.BLACK);
                addSelectedCard(CardId);
                numberOfSelectedCards++;

            }

            else if(card.isSelected()&&numberOfSelectedCards>0){
                card.select();
                button.setBackgroundColor(Color.WHITE);
                removeSelectedCard(CardId);
                numberOfSelectedCards--;
            }

        assert (numberOfSelectedCards<=3&&numberOfSelectedCards>=0);
        }
    };

    //Mémorise la sélection d'une carte
    private void addSelectedCard (int card){
        for(int i =0;i<3;i++){
        if (selection[i]==-1) {
            selection[i]=card;
            break;
        }
        }
    }

    //Déselectionne une carte en mémoire
    private void removeSelectedCard (int card){
        for(int i =0;i<3;i++){
            if (selection[i]==card) {
                selection[i]=-1;
                break;
            }
        }
    }

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
           row.setBackgroundColor(Color.BLACK);
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
            Card.setBackgroundColor(Color.WHITE);
            Card.setOnClickListener(selectedListener);
            row.addView(Card);
            }
        }

        //Initialisation des cartes sélectionnées (au début il n'y en a aucune)
        for (int i=0;i<3;i++){
            selection[i]=-1;
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
