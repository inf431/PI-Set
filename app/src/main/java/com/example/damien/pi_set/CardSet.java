package com.example.damien.pi_set;

import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Damien on 15/03/2015.
 */
public class CardSet {

    private int nCard;
    private int[] cards;

    //test indique le nombre de cartes : il vaut true si on a 12 cartes, false si 15
    // Ce constructeur ne doit etre appellé qu'une fois, en cas de changement dans les cartes on appelle reload
    public CardSet(boolean test,TableLayout table){
        nCard=test?12:15;
        cards=new int[15];

        for(int i=12;i<15;i++){
            cards[i]=-1;
        }

          // ! Ne fonctionne que tant qu'on a les cartes en 3 rangées de 4 avec eventuellement 3 de plus au debut de la quatrieme rangée
        for(int i=0;i<nCard;i++){
            cards[i]=((CardDrawable)((ImageButton)((TableRow) table.getChildAt(i / 4)).getChildAt(i % 4)).getDrawable()).getCard();
        }


    }

    // Indique si l'ensemble de cartes contient un set
    public boolean containsSet(){
        for(int i=0;i<nCard-2;i++){
            for(int j=i+1;j<nCard-1;j++){
                for(int k=j+1;k<nCard;k++){
                  if(Cards.isSet(cards[i],cards[j],cards[k])){
                        return true;
                    }

                }
            }
        }

        return false;
    }
    public void setN(int nCard){
        this.nCard=nCard;
    }
    public void reload(TableLayout table){

        for(int i=12;i<15;i++){
            cards[i]=-1;
        }
        // ! Ne fonctionne que tant qu'on a les cartes en 3 rangées de 4 avec eventuellement 3 de plus au debut de la quatrieme rangée
        for(int i=0;i<12;i++){
            cards[i]=((CardDrawable)((ImageButton)((TableRow) table.getChildAt(i / 4)).getChildAt(i % 4)).getDrawable()).getCard();
        }
    }
}
