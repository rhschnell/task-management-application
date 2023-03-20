package client.scenes.MainScreens;

import client.scenes.MainCtrl;
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

    public void join() {
        mainCtrl.getWorkspaceCtrl().loadBoard();
        ((Stage)keyField.getScene().getWindow()).close();
    }

    public TextField getKeyField() {
        return keyField;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
