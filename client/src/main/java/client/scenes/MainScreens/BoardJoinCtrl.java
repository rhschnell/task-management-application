package client.scenes.MainScreens;

import client.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardJoinCtrl implements Initializable {

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @FXML
    private TextField keyField;

    /**
     * Constructor for BoardJoin
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public BoardJoinCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Join and call load
     */
    public void join() {
        mainCtrl.getWorkspaceCtrl().setPassedKey(keyField.getText());
        mainCtrl.getWorkspaceCtrl().loadBoard();
        ((Stage)keyField.getScene().getWindow()).close();
    }

    /**
     * Getter for keyField
     * @return keyField
     */
    public TextField getKeyField() {
        return keyField;
    }

    /**
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

    }
}
