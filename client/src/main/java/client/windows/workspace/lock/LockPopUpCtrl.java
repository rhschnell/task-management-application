package client.windows.workspace.lock;

import client.windows.workspace.boardCell.BoardCellCtrl;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LockPopUpCtrl {
    private Board board;
    private BoardCellCtrl caller;

    @FXML
    private TextField inputField;

    /**
     * Constructor
     */
    public LockPopUpCtrl() {
    }

    /**
     * Method called when cancel button pressed
     */
    public void cancel() {
        ((Stage)inputField.getScene().getWindow()).close();
    }

    /**
     * Method called when confirm button pressed
     */
    public void confirm() {
        ((Stage)inputField.getScene().getWindow()).close();

    }

    // SETTERS AND GETTERS

    /**
     * Setter for the board this popUp applies to
     * @param board board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for original creator
     * @param caller the creator
     */
    public void setCaller(BoardCellCtrl caller) {
        this.caller = caller;
    }
}
