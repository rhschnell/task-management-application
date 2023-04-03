package client.windows.workspace.lock;

import client.windows.workspace.boardCell.BoardCellCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LockPopUpCtrl {
    private final LockPopUpService service;

    private Board board;
    private BoardCellCtrl caller;

    @FXML private TextField inputField;
    @FXML private Label errorMsg;

    /**
     * Constructor
     */
    @Inject
    public LockPopUpCtrl(LockPopUpService service) {
        this.service = service;
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
        // If board is protected, verify the right password was entered
        if (board.isProtected()) {
            boolean correctPassword = service.verifyPassword(board, inputField.getText());

            // If correct password entered, close the popUp and unlock the board
            if (correctPassword) {
                cancel();
                service.setProtected(false);
            }
            // If incorrect password entered, show error message.
            else {
                errorMsg.setVisible(true);
            }
        }
        // If board is NOT protected, add the entered password to the board
        else {
            cancel();
            service.setPassword(inputField.getText());
            service.setProtected(true);
        }
    }



    // SETTERS AND GETTERS
    /**
     * Setter for the board this popUp applies to
     * @param board board
     */
    public void setBoard(Board board) {
        this.board = (board);
        service.setBoard(board);
    }

    /**
     * Setter for original creator
     * @param caller the creator
     */
    public void setCaller(BoardCellCtrl caller) {
        this.caller = caller;
    }
}
