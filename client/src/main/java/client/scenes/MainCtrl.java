package client.scenes;

import client.scenes.CardWindows.AddCardCtrl;
import client.scenes.ListManagement.ListCtrl;
import client.scenes.MainScreens.BoardJoinCtrl;
import client.scenes.MainScreens.LoginCtrl;
import client.scenes.MainScreens.WorkspaceCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {
    private Stage primaryStage;
    private Scene login;
    private Scene workspace;
    private Scene boardJoin;
    private LoginCtrl loginCtrl;
    private WorkspaceCtrl workspaceCtrl;
    private BoardJoinCtrl boardJoinCtrl;

    /**
     * Initializes the Stages that needs to be switched within the app.
     *
     * @param primary   represents the primary stage
     * @param login     represents the pair of login scene, and its controller.
     * @param workspace represents the pair of workspace scene, and its controller
     */
    public void initialize(Stage primary,
                           Pair<LoginCtrl, Parent> login,
                           Pair<WorkspaceCtrl, Parent> workspace,
                           Pair<BoardJoinCtrl, Parent> boardJoin) {
        this.primaryStage = primary;

        this.loginCtrl = login.getKey();
        this.login = new Scene(login.getValue());

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        this.boardJoinCtrl = boardJoin.getKey();
        this.boardJoin = new Scene(boardJoin.getValue());

        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        setLogin();
        primary.show();
    }

    /**
     * Switches the actual scene to the login scene.
     */
    public void setLogin() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(login);
    }

    /**
     * Switches the actual scene to the workspace scene.
     */
    public void setWorkspace() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(workspace);
    }

    public LoginCtrl getLoginCtrl() {
        return loginCtrl;
    }

    public WorkspaceCtrl getWorkspaceCtrl() {
        return workspaceCtrl;
    }

    public BoardJoinCtrl getBoardJoinCtrl() {
        return boardJoinCtrl;
    }

    public Scene getBoardJoin() {
        return boardJoin;
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     * @return
     */
    public void popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.setResizable(false);
        popUp.showAndWait();
    }
}
