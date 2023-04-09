package client.windows.workspace.boardCell;

import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardCellCtrl implements Initializable {
    private WorkspaceCtrl workspaceCtrl;
    private Board board;

    private final HelperMethods helperMethods;

    @FXML
    private ImageView protectionIcon;
    @FXML
    private Label boardTitle;
    @FXML
    private ImageView leaveDeleteIcon;

    /**
     * Creates a new instance of BoardCellCtrl
     * @param helperMethods The instance of HelperMethods used for utilities
     */
    @Inject
    public BoardCellCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    /**
     * Handles the user leaving the board by clicking the dedicated icon
     */
    public void leaveBoard() {
        workspaceCtrl.leaveBoard(board);
    }

    /**
     * Handles the user pressing the title of the board to show it
     */
    public void showMyBoard() {
        workspaceCtrl.showBoard(board.getKey());
    }

    /**
     * Sets the workspace controller that this controller links back to
     * @param workspaceCtrl The workspace controller to set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
        if (workspaceCtrl.isAdmin()) {
            leaveDeleteIcon.setImage(new Image("/client/icons/trash-can.png"));
        }
    }

    /**
     * Setter for the board
     * @param board The board to set
     */
    public void setBoard(Board board) {
        this.board = board;
        this.boardTitle.setText(board.getTitle());
        updateProtectionIcon();
    }

    /**
     * Getter for the board
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leaveDeleteIcon.setCursor(Cursor.HAND);
        protectionIcon.setCursor(Cursor.HAND);
    }

    /**
     * Updates the protection icon to mirror the state of protection that the board is in (locked
     * or unlocked) and sets the hover animations indication that the user can lock/unlock the board
     */
    public void updateProtectionIcon() {
        if (board.verifyPassword("")) {
            protectionIcon.setOnMouseEntered(l -> {
                Image lockSymbol = new Image("/client/icons/lock.png");
                protectionIcon.setImage(lockSymbol);
            });

            protectionIcon.setOnMouseExited(l -> {
                Image lockSymbol = new Image("/client/icons/unlock.png");
                protectionIcon.setImage(lockSymbol);
            });
            protectionIcon.setImage(new Image("/client/icons/unlock.png"));
        } else {
            protectionIcon.setOnMouseEntered(l -> {
                Image lockSymbol = new Image("/client/icons/unlock.png");
                protectionIcon.setImage(lockSymbol);
            });

            protectionIcon.setOnMouseExited(l -> {
                Image lockSymbol = new Image("/client/icons/lock.png");
                protectionIcon.setImage(lockSymbol);
            });
            protectionIcon.setImage(new Image("/client/icons/lock.png"));
        }
    }

    /**
     * Method to change locked state of board
     */
    public void swapLock() {
        if (board.verifyPassword("")) {
            lock();
            updateProtectionIcon();
        }
    }

    /**
     * Sets the board to be unlocked and updates the icons
     */
    public void unlock() {
        workspaceCtrl.lockUnlock(board, "unlock");
        updateProtectionIcon();
    }

    /**
     * Sets the board to be locked and updates the icons
     */
    public void lock() {
        if (!board.isProtected()) {
            workspaceCtrl.lockUnlock(board, "lock");
        }
        updateProtectionIcon();
    }

    /**
     * Leaves or deletes a board depending on whether we are in the admin workspace or the user one
     */
    public void leaveDelete() {
        if (workspaceCtrl.isAdmin()) {
            workspaceCtrl.deleteScreen(board);
        } else {
            leaveBoard();
        }
    }
}

