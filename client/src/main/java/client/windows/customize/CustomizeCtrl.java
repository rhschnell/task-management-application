package client.windows.customize;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class CustomizeCtrl {
    private WorkspaceCtrl workspaceCtrl;

    private Board board;
    private BoardUtils boardUtils;

    private List<CardList> lists;
    private CardListUtils cardListUtils;


    @FXML
    private Button resetBoardColorButton;
    @FXML
    private Button resetListColorButton;
    @FXML
    private Button closeButton;

    @FXML
    private ColorPicker boardBackgroundColor;
    @FXML
    private ColorPicker boardFontColor;
    @FXML
    private ColorPicker listBackgroundColor;
    @FXML
    private ColorPicker listFontColor;

    @FXML
    private VBox cardPresets;

    /**
     * Constructor for CustomizeCtrl
     * @param workspaceCtrl a WorkspaceCtrl instance
     * @param boardUtils a BoardUtils instance
     * @param cardListUtils a CardListUtils instance
     */
    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, BoardUtils boardUtils, CardListUtils cardListUtils) {
        this.workspaceCtrl = workspaceCtrl;
        this.boardUtils = boardUtils;
        this.cardListUtils = cardListUtils;
    }

    /**
     * Sets the workspace controller that this customization popup stems from
     * @param workspaceCtrl The corresponding WorkspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;

        // Also initialize the default colors from this workspace
        listBackgroundColor.setValue(Color.web(workspaceCtrl.getInitialListColor()));
        listFontColor.setValue(Color.web(workspaceCtrl.getInitialListFontColor()));
    }

    /**
     * Setter for the board background color
     */
    @FXML
    public void setBoardBackgroundColor() {
        board.setBackgroundColour(boardBackgroundColor.getValue().toString().substring(2, 8));
    }

    /**
     * Setter for the board font color
     */
    @FXML
    public void setBoardFontColor() {
        board.setFontColour(boardFontColor.getValue().toString().substring(2, 8));
    }

    /**
     * Method to set the list background color for all the lists
     */
    @FXML
    public void setListBackgroundColor() {
        String newColor = listBackgroundColor.getValue().toString().substring(2, 8);
        for (CardList list : lists) {
            list.setBackgroundColor(newColor);
        }
        // Also make sure that any lists that are going to be created after this are going to get
        // this color out of the box
        workspaceCtrl.setInitialListColor(newColor);
    }

    /**
     * Getter to get the list background color
     *
     * @return The current background color of the lists
     */
    @FXML
    public String getListBackgroundColor() {
        return listBackgroundColor.getValue().toString();
    }

    /**
     * Method to set the list font color for all the lists
     */
    @FXML
    public void setListFontColor() {
        String newColor = listFontColor.getValue().toString().substring(2, 8);
        for (CardList list : lists) {
            list.setFontColor(newColor);
        }

        // Also make sure that any text in lists that are going to be created after this is
        // going to get this color out of the box
        workspaceCtrl.setInitialListFontColor(newColor);
    }

    /**
     * Getter to get the list font color
     *
     * @return The current font color of the list
     */
    @FXML
    public String getListFontColor() {
        return listFontColor.getValue().toString();
    }

    /**
     * Resets the colors of the board to default
     */
    @FXML
    public void resetBoard() {
        board.setFontColour("000000");
        board.setBackgroundColour("FFFFFF");
        boardUtils.insertBoard(board);
        boardBackgroundColor.setValue(Color.web(board.getBackgroundColour()));
        boardFontColor.setValue(Color.web(board.getFontColour()));
    }

    /**
     * Method to reset the font and background colors of the lists
     */
    public void resetLists() {
        String defaultBackground = "FFFFFF";
        String defaultFont = "000000";
        for (CardList list : lists) {
            list.setFontColor(defaultFont);
            list.setBackgroundColor(defaultBackground);
        }
        listBackgroundColor.setValue(Color.web(defaultBackground));
        listFontColor.setValue(Color.web(defaultFont));

        workspaceCtrl.setInitialListFontColor(defaultFont);
        workspaceCtrl.setInitialListColor(defaultBackground);
    }

    /**
     * Closes the window/stage
     */
    public void close() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    /**
     * Method to save the current made changes to the board colors
     */
    public void save() {
        for (CardList list : lists) {
            cardListUtils.insertCardList(list);
        }
        boardUtils.insertBoard(board);
        workspaceCtrl.refreshWorkspace(false);
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    /**
     * Getter for the board
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Setter for the board
     * @param shownBoard the board to be set
     */
    public void setBoard(Board shownBoard) {
        board = shownBoard;
        boardBackgroundColor.setValue(Color.web(board.getBackgroundColour()));
        boardFontColor.setValue(Color.web(board.getFontColour()));
    }

    /**
     * Method to set the cardlists that are shown on the board
     *
     * @param cardLists The cardlists
     */
    public void setLists(List<CardList> cardLists) {
        lists = cardLists;

        if (!lists.isEmpty()) {
            // Set the color pickers to the corresponding colors of the (first) cardlist
            listBackgroundColor.setValue(Color.web(lists.get(0).getBackgroundColor()));
            listFontColor.setValue(Color.web(lists.get(0).getFontColor()));
        } else {
            // The lists might be empty, but the initial color might be set already.
            //TODO: implement this functionality
        }
    }
}

