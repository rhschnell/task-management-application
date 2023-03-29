package client;

import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.adminview.boardSpace.AdminCtrl;
import client.windows.login.admin.AdminLoginCtrl;
import client.windows.login.start.StartUpCtrl;
import client.windows.login.user.UserLoginCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {
    private Stage primaryStage;
    private Scene startUp;
    private Scene userLogin;
    private Scene adminLogin;
    private Scene workspace;
    private Scene adminView;
    private StartUpCtrl startUpCtrl;
    private UserLoginCtrl userLoginCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private WorkspaceCtrl workspaceCtrl;

    private WorkspaceCtrl adminViewCtrl;
    private HelperMethods hm;

    /**
     * Initializes the Stages that needs to be switched within the app.
     *
     * @param primary       represents the primary stage
     * @param adminLogin    represents the pair of login scene, and its controller.
     * @param workspace     represents the pair of workspace scene, and its controller
     */
    public void initialize(Stage primary,
                           Pair<StartUpCtrl, Parent> startUp,
                           Pair<UserLoginCtrl, Parent> userLogin,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           Pair<WorkspaceCtrl, Parent> workspace,
                           Pair<AdminCtrl, Parent> adminView,
                           HelperMethods hm) {
        this.primaryStage = primary;

        this.startUpCtrl = startUp.getKey();
        this.startUp = new Scene(startUp.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        this.userLoginCtrl = userLogin.getKey();
        this.userLogin = new Scene(userLogin.getValue());

        this.adminViewCtrl = workspace.getKey();
        this.adminView = new Scene(adminView.getValue());

        primary.setTitle("Talio");
        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        this.hm = hm;
        hm.setScenes(this.startUp, this.adminLogin, this.workspace, this.userLogin, this.adminView);
        hm.setPrimaryStage(primaryStage);
        hm.setScene(Scenes.STARTUP);
        hm.setCardFormat(new DataFormat("card"));
        primary.show();
    }
}
