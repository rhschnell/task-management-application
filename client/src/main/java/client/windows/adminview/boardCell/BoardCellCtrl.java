package client.windows.adminview.boardCell;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.adminview.boardSpace.AdminCtrl;
import client.windows.adminview.deleteBoard.DeleteBoardCtrl;
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
    public BoardCellCtrl() {
    }

    /**
     * Sets the corresponding AdminCtrl
     * @param adminCtrl The AdminCtrl to set
     */
    public void setAdminCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
    }

    /**
     * Shows the board to which this cell links
     */
    public void showMyBoard() {
        adminCtrl.showBoard(board.getKey());
    }

    /**
     * Setter for the board
     * @param board The new board
     */
    public void setBoard(Board board) {
        this.board = board;
        this.boardTitle.setText(board.getTitle());
    }

    /**
     * Gets the board associated to this cell
     * @return The board associated to this cell
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Handles deletion of this board by displaying a popup that asks the user for confirmation
     */
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


    /**
     * Initializes the controller
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
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

