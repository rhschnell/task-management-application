package client.windows.workspace.boardCell;

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

    @FXML
    private ImageView protectionIcon;
    @FXML
    private Label boardTitle;
    @FXML
    private ImageView leaveIcon;

    /**
     * Creates a new instance of ListCellCtrl
     */
    @Inject
    public BoardCellCtrl() {

    }

    public void leaveBoard() {
        workspaceCtrl.leaveBoard(board);
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
        leaveIcon.setCursor(Cursor.HAND);
        protectionIcon.setCursor(Cursor.HAND);

        // TODO: if(board.isProtected())
        protectionIcon.setOnMouseEntered(l -> {
            Image lockSymbol = new Image("/client/icons/lock.png");
            protectionIcon.setImage(lockSymbol);
        });

        protectionIcon.setOnMouseExited(l -> {
            Image lockSymbol = new Image("/client/icons/unlock.png");
            protectionIcon.setImage(lockSymbol);
        });
        // TODO: else
    }
//
//    public void setLabelColour()
//    {
//        this.boardTitle.setTextFill(Color.web(board.getFontColour()));
//    }
}

