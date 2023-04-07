package client.windows.workspace.leave;

import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Set;

public class LeaveCtrl {

    @FXML
    private Button leaveButton;

    private WorkspaceCtrl workspaceCtrl;
    private HelperMethods hm;
    private Set<String> joinedKeys;
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
     * @param workspaceCtrl The new workspace controller
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Sets the HelperMethods
     * @param hm the new helper methods instance
     */
    public void setHelperMethods(HelperMethods hm) {
        this.hm = hm;
    }

    /**
     * Sets the joinedKeys
     * @param joinedKeys The new joined keys list
     */
    public void setJoinedKeys(Set<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }

    /**
     * Sets the leaveBoard
     * @param leaveBoard The board to set as leaveBoard
     */
    public void setLeaveBoard(Board leaveBoard) {
        this.leaveBoard = leaveBoard;
    }
}
