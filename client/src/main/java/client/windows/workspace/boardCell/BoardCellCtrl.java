package client.windows.workspace.boardCell;

import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class BoardCellCtrl {
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

    /**
     * Setter for the board
     */
    public void setBoard(Board board) {
        this.board = board;
        this.boardTitle.setText(board.getTitle());
    }

    public Board getBoard() {
        return board;
    }
}

