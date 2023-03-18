package client.scenes.MainScreens;

import client.utils.ControllerCommunicater;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BoardJoinCtrl {

    private final ServerUtils server;
    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private TextField keyField;


    /**
     * Constructor with no parameters for AddCardCtrl
     */
    public BoardJoinCtrl() {
        this.workspaceCtrl = new WorkspaceCtrl();
        this.server = new ServerUtils();
    }

    /**
     * Constructor for AddCardCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public BoardJoinCtrl(ServerUtils server, WorkspaceCtrl mainCtrl) {
        this.workspaceCtrl = mainCtrl;
        this.server = server;
    }

    public void join() {
        ControllerCommunicater.setKey(keyField.getText());
        workspaceCtrl.loadBoard();
        ((Stage)keyField.getScene().getWindow()).close();
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }
}
