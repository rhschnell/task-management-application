package client.windows.lists.cells;

import client.utils.HelperMethods;
import client.windows.lists.list.ListCtrl;
import commons.Card;
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
    private final HelperMethods helperMethods;

    /**
     * Constructor for QuickAddCardCtrl
     * @param service a CardService instance
     * @param listCtrl a ListCtrl instance
     * @param helperMethods Injected instance of HelperMethods
     */
    @Inject
    public QuickAddCardCtrl(CardService service, ListCtrl listCtrl, HelperMethods helperMethods) {
        this.service = service;
        this.listCtrl = listCtrl;
        this.helperMethods = helperMethods;
    }

    /**
     * Displays the add button
     */
    public void setAddButtonVisible() {
        this.addButton.setVisible(true);
        this.addButton.setDisable(false);
    }

    /**
     * Adds a new card with a title
     */
    public void addCard() {
        String title = helperMethods.getInputValidator().stripWhitespace(cardTitle.getText());

        if (!helperMethods.validateInputAndShowPopup(title)) return;

        service.insertCard(new Card(title),listCtrl.getCardList());
        listCtrl.displayCards();
    }

    /**
     * Set the ListCtrl the QuickAdd is on
     * @param listCtrl the listCtrl that needs to be setted
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    /**
     * Getter for the key of the board
     * @return the key of the board
     */
    public String getBoardKey()
    {
        return service.getBoardKey();
    }

    /**
     * Setter for the key of the board
     * @param boardKey the key of the board
     */
    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }

}
