package client.windows.workspace.leave;

import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

public class LeaveCtrl {

    @FXML
    private Button leaveButton;

    private WorkspaceCtrl workspaceCtrl;
    private HelperMethods hm;
    private List<String> joinedKeys;
    private Board leaveBoard;

    @Inject
    public LeaveCtrl() {
    }

    public void leave() {
        joinedKeys.remove(leaveBoard.getKey());
        workspaceCtrl.refreshWorkspace(true);
        if (leaveBoard.equals(workspaceCtrl.getShownBoard())) {
            workspaceCtrl.clearWorkspace();
        }
        hm.getMemMap().get(hm.getServerIP()).remove(leaveBoard.getKey());

        ((Stage) leaveButton.getScene().getWindow()).close();
    }

    public void cancel() {
        ((Stage) leaveButton.getScene().getWindow()).close();
    }

    // SETTERS FOR INJECTION

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    public void setHelperMethods(HelperMethods hm) {
        this.hm = hm;
    }

    public void setJoinedKeys(List<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }

    public void setLeaveBoard(Board leaveBoard) {
        this.leaveBoard = leaveBoard;
    }
}
