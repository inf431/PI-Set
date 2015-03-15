package com.example.damien.pi_set;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    //M'en sers pas pour l'instant
    Handler handler = new Handler();
    private LinkedList<Integer> deck;
    //Trois champs pour mémoriser la sélection du joueur, ainsi que le nombre de cartes sélectionnées
    //Le cas selection[i]=-1 correspond au cas où moins de 3 cartes sont sélectionnées
    private int[] selection = new int[3];
    private ImageButton[] selectionViews = new ImageButton[3];
    private int numberOfSelectedCards=0;
    private Button boutonTest;
    private int score=0;

    //Gère la transition à effectuer lorsque 3 cartes sont sélectionnées

    public void transition() {

        int color = (Cards.isSet(selection[0], selection[1], selection[2]))? Color.GREEN : Color.RED;

        for (int i = 0; i<3; i++){
             selectionViews[i].setBackgroundColor(color);

            }




        for (int i = 0; i<3; i++){

            selectionViews[i].setBackgroundColor(Color.WHITE);
            removeSelectedCard(selection[i],selectionViews[i]);
        }

     }






    //Gère la sélection d'une carte par le joueur
    private OnClickListener selectedListener = new OnClickListener(){
        public void onClick(View v){


            ImageButton button = (ImageButton) v;
            CardDrawable card = (CardDrawable) button.getDrawable();
            int CardId = card.getCard();

            if (!card.isSelected()&&numberOfSelectedCards<3) {
                button.setBackgroundColor(Color.BLACK);
                addSelectedCard(CardId,button);

            }

            else if(card.isSelected()&&numberOfSelectedCards>0){
                button.setBackgroundColor(Color.WHITE);
                removeSelectedCard(CardId, button);

            }

        assert (numberOfSelectedCards<=3&&numberOfSelectedCards>=0);


        }
    };
    private OnClickListener testListener = new OnClickListener(){
        public void onClick(View v){

        if(numberOfSelectedCards==3){
           if(Cards.isSet(selection[0],selection[1],selection[2])){
                score++;

                TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f);

                for(int i=0;i<3;i++) {
                    TableRow row = (TableRow) selectionViews[i].getParent();

                    row.removeView(selectionViews[i]);
                    if(!deck.isEmpty()) {
                        ImageButton Card = new ImageButton(getApplicationContext());
                        Card.setImageDrawable(new CardDrawable(deck.pop()));
                        Card.setLayoutParams(param2);
                        Card.setBackgroundColor(Color.WHITE);
                        Card.setOnClickListener(selectedListener);
                        row.addView(Card);
                    }
                    removeSelectedCard(selection[i],selectionViews[i]);
                }
            }
            else{
               boutonTest.setText("Raté !");
           }
        }


        }
    };

    //Mémorise la sélection d'une carte
    private void addSelectedCard (int card,ImageButton v){
        ((CardDrawable) v.getDrawable()).select();
        for(int i =0;i<3;i++){
        if (selection[i]==-1) {
            selection[i]=card;
            numberOfSelectedCards++;
            break;
        }
        }
        for(int i =0;i<3;i++){
            if (selectionViews[i]==null) {
                selectionViews[i]=v;
                break;
            }
        }
    }

    //Déselectionne une carte en mémoire
    private void removeSelectedCard (int card, ImageButton v){
        ((CardDrawable) v.getDrawable()).select();
        for(int i =0;i<3;i++) {
            if (selection[i] == card) {
                selection[i] = -1;
                numberOfSelectedCards--;
                break;
            }
        }
            for(int i =0;i<3;i++){
                if (selectionViews[i] != null && selectionViews[i].equals(v)) {
                    selectionViews[i]=null;
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
            selectionViews[i]=null;
        }

        // Creation du bouton de test des cartes.
        boutonTest=new Button(this.getApplicationContext());
        boutonTest.setText("Set ?");

        boutonTest.setOnClickListener(testListener);

        //Creation de la 4eme ligne
        TableRow row=new TableRow(getApplicationContext());
        row.setWeightSum(4.0f);
        row.setBackgroundColor(Color.BLACK);
        row.setLayoutParams(param);
        row.addView(boutonTest);
        table.addView(row);


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
