package client;

import commons.Board;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;

public class TestingClass {

    /**
     * Creates a new Board Object in order to help us while testing functionalities.
     * @param boardName represents the name of the board that will be created.
     * @return
     */
    public Board createBoardObject(String boardName)
    {
        ArrayList<CardList> systemCardList= new ArrayList<>();
        systemCardList.add(createCardListObject("TODO"));
        systemCardList.add(createCardListObject("DONE"));
        systemCardList.add(createCardListObject("TRASH"));
        return new Board(-1, boardName, systemCardList);
    }

    /**
     * Creates a CardList Object in order to help us while testing functionalities.
     * @param cardListTitle represents the name of the CardList that will be created.
     * @return a new CardList Object
     */
    public CardList createCardListObject(String cardListTitle)
    {
        Card p1 = new Card("Cleaning","To clean the floor","white",new ArrayList<>(),new ArrayList<>());
        Card p2 = new Card("Working","To work for the company","gray",new ArrayList<>(),new ArrayList<>());
        Card p3 = new Card("Washing","De wash the clothes","white",new ArrayList<>(),new ArrayList<>());
        Card p4 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p5 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p6 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p7 = new Card("Cleaning","To clean the floor","white",new ArrayList<>(),new ArrayList<>());
        Card p8 = new Card("Working","To work for the company","gray",new ArrayList<>(),new ArrayList<>());
        Card p9 = new Card("Washing","To wash the clothes","white",new ArrayList<>(),new ArrayList<>());

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(p1);
        cards.add(p2);
        cards.add(p3);
        cards.add(p4);
        cards.add(p5);
        cards.add(p6);
        cards.add(p7);
        cards.add(p8);
        cards.add(p9);
        return new CardList(cardListTitle,cards);
    }
}
