package com.example.damien.pi_set;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {
    public int DECK_SIZE=81;

    static Handler handler = new Handler();
    private LinkedList<Integer> deck;
    //Trois champs pour mémoriser la sélection du joueur, ainsi que le nombre de cartes sélectionnées
    //Le cas selection[i]=-1 correspond au cas où moins de 3 cartes sont sélectionnées
    private int[] selection = new int[3];
    private ImageButton[] selectionViews = new ImageButton[3];
    private int numberOfSelectedCards=0;
    private CardSet set;
    private int nCard;

  //  private Button boutonTest;
    private int score=0;

    static Timer timer = new Timer();

    TableLayout boutonTest;
    TextView scoreString;
    static TextView timerTextView;






    public Runnable transition = new Runnable() {
        public void run (){

            int color = (Cards.isSet(selection[0], selection[1], selection[2])) ? Color.GREEN : Color.RED;

                // Ajout au bouton du dernier set attrapé
            if(color==Color.GREEN) {
                TableRow row = (TableRow) boutonTest.getChildAt(3);
                row.setWeightSum(3.0f);
                TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                       0,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                row.removeAllViews();
                for (int j = 0; j < 3; j++) {

                    ImageButton Card=new ImageButton(getApplicationContext());
                    Card.setImageDrawable(selectionViews[j].getDrawable());
                    Card.setLayoutParams(param2);
                    Card.setBackgroundColor(Color.WHITE);
                    Card.setPadding(1,1,1,1);
                    row.addView(Card,j);
                }

                for (int j = 0; j < 3; j++) {
                    row.getChildAt(j).invalidate();
                }
                row.invalidate();
            }

            for (int i = 0; i < 3; i++) {
                selectionViews[i].setBackgroundColor(color);

            }


        }

    };

// Objet qui gère lorsqu'on a trouvé un Set
    public Runnable newDeal = new Runnable(){
        public void run (){

            score++;

            scoreString.setText("Score : "+score);

            TableLayout table = (TableLayout) findViewById (R.id.tableLayout1);

            TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.MATCH_PARENT, 1.0f);

            if(nCard==12) {
                for (int i = 0; i < 3; i++) {
                    TableRow row = (TableRow) selectionViews[i].getParent();
                    row.removeView(selectionViews[i]);

                    if (!deck.isEmpty()) {
                        ImageButton Card = new ImageButton(getApplicationContext());
                        Card.setImageDrawable(new CardDrawable(deck.pop()));
                        Card.setLayoutParams(param2);
                        Card.setBackgroundColor(Color.WHITE);
                        Card.setOnClickListener(selectedListener);
                        row.addView(Card);
                    }
                    removeSelectedCard(selection[i], selectionViews[i]);
                }

            }

            else if(nCard==15){
                TableRow row =(TableRow) table.getChildAt(3);




                for(int i=0;i<3;i++){

                    remplaceCartes(selection[i],((CardDrawable)((ImageButton) row.getChildAt(i)).getDrawable()).getCard(),table);

                    row.removeView(row.getChildAt(i));

                    //Creation d'une image blanche la ou on a supprimé les 3 cartes
                    ImageView img=new ImageView(getApplicationContext());

                    ShapeDrawable shape=new ShapeDrawable();
                    shape.getPaint().setColor(Color.WHITE);
                    img.setImageDrawable(shape);
                    img.setBackgroundColor(Color.WHITE);
                    img.setLayoutParams(param2);
                    row.addView(img,i);

                    removeSelectedCard(selection[i], selectionViews[i]);
                }
                row.invalidate();

            }



            // On vérifie qu'on a bien un set.
            set.setN(12);
            set.reload(table);
            if(!set.containsSet())
                ajoute3Cartes((TableRow) table.getChildAt(3));


        }

    };

    public Runnable failUnSelect = new Runnable() {
        public void run(){
            for(int i =0;i<3;i++){
                selectionViews[i].setBackgroundColor(Color.WHITE);
                removeSelectedCard(selection[i],selectionViews[i]);
            }

        }
    };





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

    //Gere l'appui sur le bouton Test

    private OnClickListener testListener = new OnClickListener(){
        public void onClick(View v){

        if(numberOfSelectedCards==3){
           if(Cards.isSet(selection[0],selection[1],selection[2])){

               handler.post(transition);
               handler.postDelayed(newDeal,500);




            }
            else{
               handler.post(transition);
               handler.postDelayed(failUnSelect,500);


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

    private void ajoute3Cartes(TableRow row){
        nCard=15;

            TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.MATCH_PARENT,1.0f);
        for(int j=0;j<3;j++) {
            if(!deck.isEmpty()) {
                ImageButton Card = new ImageButton(getApplicationContext());
                Card.setImageDrawable(new CardDrawable(deck.pop()));
                Card.setLayoutParams(param2);
                Card.setBackgroundColor(Color.WHITE);
                Card.setOnClickListener(selectedListener);
                row.removeView(row.getChildAt(j));
                row.addView(Card, j);
            }
        }
    }
    //Remplacer la carte i par la carte j dans les 3 premieres lignes de la table
    private void remplaceCartes(int i,int j, TableLayout table){
        TableRow row;
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT,1.0f);

        for(int k=0;k<3;k++){
            row=(TableRow)table.getChildAt(k);
            for(int l=0;l<4;l++){
                if(i==((CardDrawable)((ImageButton) row.getChildAt(l)).getDrawable()).getCard()){
                    ImageButton Card = new ImageButton(getApplicationContext());
                    row.removeView(row.getChildAt(l));
                    Card.setImageDrawable(new CardDrawable(j));
                    Card.setLayoutParams(param2);
                    Card.setBackgroundColor(Color.WHITE);
                    Card.setOnClickListener(selectedListener);

                    row.addView(Card, l);
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table=(TableLayout)findViewById(R.id.tableLayout1);


        // Création et mélange du paquet de cartes
        deck=new LinkedList<>();
        for(int i=0;i<DECK_SIZE;i++){
            deck.add(Cards.correctRange(i));
        }
        Collections.shuffle(deck);

        // Ajout de lignes dans lesquels on affiche les cartes
        TableLayout.LayoutParams param = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                0, 1.0f);
       for(int i=0;i<3;i++){
           TableRow row=new TableRow(getApplicationContext());
           row.setWeightSum(4.0f);
           row.setBackgroundColor(Color.BLACK);
           row.setLayoutParams(param);
           table.addView(row);
       }

        //Ajout de cartes aux lignes
        nCard=12;
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(
               0,
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

        // Creation du bouton de test des cartes selectionnées


        boutonTest = new TableLayout(getApplicationContext());
        boutonTest.setWeightSum(6.0f);

        for(int i=0;i<3;i++){
            TableRow buttonRow=new TableRow(getApplicationContext());
            buttonRow.setLayoutParams(param);
            boutonTest.addView(buttonRow);
            buttonRow.setGravity(Gravity.CENTER);
        }
            // La 4 eme ligne est crée avec un poids égal a celui des 3 autres
            TableLayout.LayoutParams param3=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                0, 3.0f);
            TableRow buttonRow=new TableRow(getApplicationContext());
            buttonRow.setLayoutParams(param3);
            boutonTest.addView(buttonRow);
            buttonRow.setGravity(Gravity.CENTER);


        TextView setString= new TextView(getApplicationContext());
        setString.setText("Set ?");
        ((TableRow) boutonTest.getChildAt(0)).addView(setString);
        setString.setGravity(Gravity.CENTER);
        scoreString = new TextView (getApplicationContext());
        scoreString.setText("Score : "+score);
        scoreString.setGravity(Gravity.CENTER);
        ((TableRow)  boutonTest.getChildAt(1)).addView(scoreString);
        timerTextView = new TextView(getApplicationContext());
        timerTextView.setText("0");
        timerTextView.setGravity(Gravity.CENTER);
        ((TableRow)  boutonTest.getChildAt(2)).addView(timerTextView);

        boutonTest.setOnClickListener(testListener);
        boutonTest.setLayoutParams(param2);





        //Creation de la 4eme ligne

        TableRow row=new TableRow(getApplicationContext());
        row.setWeightSum(4.0f);
        row.setBackgroundColor(Color.BLACK);
        row.setLayoutParams(param);

        for(int i=0;i<3;i++){
            ShapeDrawable shape=new ShapeDrawable();
            shape.getPaint().setColor(Color.WHITE);
            ImageView img=new ImageView(this.getApplicationContext());
            img.setImageDrawable(shape);
            img.setBackgroundColor(Color.WHITE);
            img.setLayoutParams(param2);
            row.addView(img,i);
        }


        row.addView(boutonTest,3);

        table.addView(row);

    // On verifie qu'il existe bien un Set dans les cartes affichées
        // sinon on affiche 3 cartes de plus

        set=new CardSet(true,table);
        if(!set.containsSet()){
           ajoute3Cartes(row);

       }

    if(!timer.isAlive())
        timer.start();

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
        if (id == R.id.server_settings) {
            Intent intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }
        if (id == R.id.client_settings) {
            Intent intent = new Intent(this, ClientActivity.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}

