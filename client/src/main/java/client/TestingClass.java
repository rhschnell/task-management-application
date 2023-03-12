package client;

import commons.Board;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;

public class TestingClass {

    public Board createBoardObject(String boardName)
    {
        ArrayList<CardList> systemCardList= new ArrayList<>();
        systemCardList.add(createCardListObject("TODO"));
        systemCardList.add(createCardListObject("DONE"));
        systemCardList.add(createCardListObject("TRASH"));
        Board myBoard = new Board(boardName, "no-key", systemCardList);
        return myBoard;
    }
    public CardList createCardListObject(String cardListTitle)
    {
        Card p1 = new Card("Cleaning","To clean the floor","white",new ArrayList<>(),new ArrayList<>());
        Card p2 = new Card("Working","To work for the company","gray",new ArrayList<>(),new ArrayList<>());
        Card p3 = new Card("Washing","De wash the clothes","white",new ArrayList<>(),new ArrayList<>());
        Card p4 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p5 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p6 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(p1);
        cards.add(p2);
        cards.add(p3);
        cards.add(p4);
        cards.add(p5);
        cards.add(p6);
        cards.add(p6);
        cards.add(p6);
        cards.add(p6);
        CardList systemCard = new CardList(cardListTitle,cards);
        return systemCard;
    }
}
