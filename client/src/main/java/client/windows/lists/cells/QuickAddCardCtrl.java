package client.windows.lists.cells;

import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.lists.list.ListCtrl;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.ArrayList;

public class QuickAddCardCtrl {

    private final CardService service;
    private ListCtrl listCtrl;
    @FXML
    private TextField cardTitle;
    @FXML
    private Button addButton;
    private HelperMethods helperMethods;

    private Board shownBoard;


    /**
     * Constructor for QuickAddCardCtrl
     *
     * @param service       a CardService instance
     * @param listCtrl      a ListCtrl instance
     * @param helperMethods Injected instance of HelperMethods
     */
    @Inject
    public QuickAddCardCtrl(CardService service, ListCtrl listCtrl, HelperMethods helperMethods) {
        this.service = service;
        this.listCtrl = listCtrl;
        this.helperMethods = helperMethods;
    }

    /**
     * Gets the preset that is set as default
     *
     * @return The default preset
     */
    public CardColorPreset getDefaultPreset() {
        for (CardColorPreset preset : shownBoard.getPresetList()) {
            if (preset.isDefault()) {
                return preset;
            }
        }
        return shownBoard.getPresetList().get(0);
    }

    /**
     * Getter for the key of the board
     *
     * @return the key of the board
     */
    public String getBoardKey() {
        return service.getBoardKey();
    }

    /**
     * Setter for the key of the board
     *
     * @param boardKey the key of the board
     */
    public void setBoardKey(String boardKey) {
        service.setBoardKey(boardKey);
    }

    /**
     * Setter for hm
     *
     * @param helperMethods hm
     */
    public void setHelperMethods(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
        service.setIP(helperMethods.getServerIP());

    }

    /**
     * Set the ListCtrl the QuickAdd is on
     *
     * @param listCtrl the listCtrl that needs to be setted
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    /**
     * Sets the shownBoard
     *
     * @param shownBoard The shownBoard to be set
     */
    public void setShownBoard(Board shownBoard) {
        this.shownBoard = shownBoard;
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

        if (!checkAndHandleInput(title)) return;

        Card card = new Card(title);
        card.setPresets(new ArrayList<>());
        card.setPreset(getDefaultPreset());


        service.insertCard(card, listCtrl.getCardList());
        listCtrl.displayCards();
    }

    /**
     * Checks the user input and shows error messages accordingly
     *
     * @param title The title to check
     */
    private boolean checkAndHandleInput(String title) {
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your card title cannot be empty"));
            return false;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your card name cannot be longer than " + HelperMethods.getMaxInputLength()));
            return false;
        }
        return true;
    }
}
