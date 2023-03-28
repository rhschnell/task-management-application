package client.windows.adminview.boardCell;

import client.windows.adminview.boardSpace.AdminCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardCellCtrl  implements Initializable {
    private final BoardCellService service;
    private AdminCtrl adminCtrl;
    private Board board;

    @FXML
    private Label boardTitle;

    @FXML
    private ImageView deleteIcon;

    /**
     * Creates a new instance of ListCellCtrl
     */
    @Inject
    public BoardCellCtrl(BoardCellService service) {
        this.service = service;
    }

    public void setAdminCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteIcon.setCursor(Cursor.HAND);
    }
}

