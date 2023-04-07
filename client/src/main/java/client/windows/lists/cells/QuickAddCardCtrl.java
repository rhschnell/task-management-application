package client.windows.lists.cells;

import client.windows.lists.list.ListCtrl;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class QuickAddCardCtrl {

    private final CardService service;
    private ListCtrl listCtrl;
    @FXML
    private TextField cardTitle;
    @FXML
    private Button addButton;

    private Board shownBoard;

    @Inject
    public QuickAddCardCtrl(CardService service, ListCtrl listCtrl) {
        this.service = service;
        this.listCtrl = listCtrl;
    }

    public void setAddButtonVisible() {
        this.addButton.setVisible(true);
        this.addButton.setDisable(false);
    }

    /**
     * Adds a new card with a title
     */
    public void addCard() {
        Card card = new Card(cardTitle.getText());
        card.setBackgroundColor(shownBoard.getDefaultCardBackgroundColor());
        card.setFontColor(shownBoard.getDefaultCardFontColor());
        card.setPreset(new CardColorPreset(shownBoard.getDefaultPreset().getName(),
                shownBoard.getDefaultCardBackgroundColor(),
                shownBoard.getDefaultCardFontColor()));

        service.insertCard(card, listCtrl.getCardList());
        listCtrl.displayCards();
    }

    /**
     * Set the ListCtrl the QuickAdd is on
     * @param listCtrl the listCtrl that needs to be setted
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public String getBoardKey()
    {
        return service.getBoardKey();
    }

    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }

    public void setShownBoard(Board shownBoard) {
        this.shownBoard = shownBoard;
    }
}
