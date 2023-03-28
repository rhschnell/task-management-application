package client.windows.adminview.boardCell;

import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BoardCellCtrl {
    private final BoardCellService service;
    private Board board;

    @FXML
    private Label boardTitle;

    /**
     * Creates a new instance of ListCellCtrl
     */
    @Inject
    public BoardCellCtrl(BoardCellService service) {
        this.service = service;
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

    public void delete() {
        service.deleteBoard(this.board);
    }
}

