package client.windows.lists.list;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class ListService {
    private final CardListUtils listServer;
    private final CardUtils cardServer;
    private final BoardUtils boardUtils;
    private String boardKey;
    private CardList cardList;

    /**
     * Sets the boardKey
     * @param boardKey to set
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Gets the boardKey
     * @return the boardKey
     */
    public String getBoardKey() {
        return boardKey;
    }

    /**
     * Constructor for the ListService
     * @param listServer The server handling lists
     * @param cardServer The server handling cards
     * @param boardUtils The utility class for handling boards
     */
    @Inject
    public ListService(CardListUtils listServer, CardUtils cardServer, BoardUtils boardUtils) {
        this.listServer = listServer;
        this.cardServer = cardServer;
        this.boardUtils=boardUtils;
        this.cardList = new CardList();
    }

    public BoardUtils getBoardUtils() {
        return boardUtils;
    }

    /**
     * Refreshes the cardList so that we know we perform operations on the last version of the CardList
     */
    public void refreshCardList() {
        this.cardList = getCardList(cardList.getId());
    }

    /**
     * Gets the cardList currently stored in the service
     * @return the cardList stored in the service
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * Sets the cardList that this fxml has
     * @param cardList the cardList that needs to be inserted
     */
    public void setCardList(CardList cardList)
    {
        this.cardList = cardList;
    }

    /**
     * Renames the CardList with the new name entered by the user
     * @param newName The new name the CardList should have
     */
    public void renameCardList(String newName)
    {
        cardList.setListTitle(newName);
        insertCardList(cardList);
    }

    /**
     * Deletes from the database the card by using the position fix function from the cardList
     * object
     * @param card The card to delete from the list
     */
    public void deleteFromCardList(Card card)
    {
        cardServer.deleteCard(card.getId());
        cardServer.deleteCardFromDatabase(card.getId());
    }


    /**
     * Returns the CardList with the id
     * @param id the id the cardList has
     * @return the CardList with the rquired id
     */
    public CardList getCardList(long id)
    {
        return listServer.getCardList(id);
    }

    /**
     * Does the dragAndDrop operation into the server
     * @param draggedCard the card that has been dragged
     * @param position the position the cards should be put in the list
     */
    public void dragAndDrop(Card draggedCard,int position)
    {
        if(draggedCard.getPriority()<position && cardList.getCards().contains(draggedCard))
            position--;
        deleteFromCardList(draggedCard);
        refreshCardList();
        cardList.removeCard(draggedCard);
        cardList.addCard(draggedCard,position);
        insertCardList(cardList);
    }

    /**
     * Inserts the cardList into the server
     * @param cardList the cardList that needs to be inserted
     */
    public void insertCardList(CardList cardList) {
        listServer.insertCardList(cardList);
    }
}
