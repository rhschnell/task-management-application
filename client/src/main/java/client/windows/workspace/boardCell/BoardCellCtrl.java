package client.windows.workspace.boardCell;

import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class BoardCellCtrl {
    private WorkspaceCtrl workspaceCtrl;

    private Board board;

    @FXML
    private Label boardTitle;
    @FXML
    private ImageView deleteIcon;

    /**
     * Creates a new instance of ListCellCtrl
     */
    @Inject
    public BoardCellCtrl() {

    }

    public void showMyBoard() {
        workspaceCtrl.showBoard(board.getKey());
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Setter for the board
     */
    public void setBoard(Board board) {
        this.board = board;
        this.boardTitle.setText(board.getTitle());
    }

    /**
     * Getter for the board
     * @return board
     */
    public Board getBoard() {
        return board;
    }
}

