package client.windows.lists.list;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class ListService {
    private final CardListUtils listServer;
    private final CardUtils cardServer;

    private CardList cardList;
    @Inject
    public ListService(CardListUtils listServer, CardUtils cardServer) {
        this.listServer = listServer;
        this.cardServer = cardServer;
        this.cardList = new CardList();
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
     * Deletes from the databse the card by using the position fix function from the cardList object
     * @param card
     */
    public void deleteFromCardList(Card card)
    {
        cardServer.deleteFromCardList(card);
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
