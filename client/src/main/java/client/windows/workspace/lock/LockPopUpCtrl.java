package client.windows.workspace.lock;

import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LockPopUpCtrl {
    private final LockPopUpService service;

    private Board board;
    private WorkspaceCtrl workspace;

    @FXML private TextField inputField;
    @FXML private Label errorMsg;
    private String mode;

    /**
     * Creates a new instance of a LockPopUpCtrl
     * @param service The service that handles the locking of boards
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
        if (mode.equals("unlock")) {
            boolean correctPassword = service.verifyPassword(board, inputField.getText());

            // If correct password entered, close the popUp and unlock the board
            if (correctPassword) {
                cancel();
                board.setProtected(false);
                workspace.getPwdMap().put(board.getKey(), inputField.getText());
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
            workspace.getPwdMap().put(board.getKey(), inputField.getText());
            board.setProtected(false);
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

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setWorkspace(WorkspaceCtrl workspace) {
        this.workspace = workspace;
    }
}
