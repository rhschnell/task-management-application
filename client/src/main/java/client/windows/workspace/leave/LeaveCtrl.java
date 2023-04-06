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

    /**
     * Constructor for the LeaveCtrl
     */
    @Inject
    public LeaveCtrl() {
    }

    /**
     * Leaves the board
     */
    public void leave() {
        ((Stage) leaveButton.getScene().getWindow()).close();

        if (leaveBoard.equals(workspaceCtrl.getShownBoard())) {
            workspaceCtrl.clearWorkspace();
        }

        joinedKeys.remove(leaveBoard.getKey());
        hm.getMemMap().get(hm.getServerIP()).remove(leaveBoard.getKey());

        workspaceCtrl.refreshWorkspace(true);
    }

    /**
     * Cancels the pop-up
     */
    public void cancel() {
        ((Stage) leaveButton.getScene().getWindow()).close();
    }


    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Sets the HelperMethods
     * @param hm
     */
    public void setHelperMethods(HelperMethods hm) {
        this.hm = hm;
    }

    /**
     * Sets the joinedKeys
     * @param joinedKeys
     */
    public void setJoinedKeys(List<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }

    /**
     * Sets the leaveBoard
     * @param leaveBoard
     */
    public void setLeaveBoard(Board leaveBoard) {
        this.leaveBoard = leaveBoard;
    }
}
