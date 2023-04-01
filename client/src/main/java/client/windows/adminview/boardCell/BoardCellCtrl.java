package client.windows.adminview.boardCell;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.adminview.boardSpace.AdminCtrl;
import client.windows.adminview.deleteBoard.DeleteBoardCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class BoardCellCtrl  implements Initializable {
    private AdminCtrl adminCtrl;
    private Board board;

    @FXML
    private Label boardTitle;
    @FXML
    private ImageView deleteIcon;
    @FXML
    private ImageView protectionIcon;

    /**
     * Creates a new instance of BoardCellCtrl
     */
    @Inject
    public BoardCellCtrl(BoardCellService service) {
    }

    public void setAdminCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
    }

    public void showMyBoard() {
        adminCtrl.showBoard(board.getKey());
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
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteBoardCtrl.class,
                        "client", "windows", "adminview", "deleteboard", "DeleteBoard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setAdminCtrl(adminCtrl);
        loader.getKey().setBoardKey(board.getKey());
        HelperMethods.popUp(scene, "Delete the board");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteIcon.setCursor(Cursor.HAND);
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
}

