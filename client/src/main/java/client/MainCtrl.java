package client;

import client.scenes.MainScreens.*;
import client.scenes.UserWorkspace.WorkspaceCtrl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {
    private Stage primaryStage;
    private Scene startUp;
    private Scene userLogin;
    private Scene adminLogin;
    private Scene workspace;
    private StartUpCtrl startUpCtrl;
    private UserLoginCtrl userLoginCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private WorkspaceCtrl workspaceCtrl;

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
                           Pair<WorkspaceCtrl, Parent> workspace) {
        this.primaryStage = primary;

        this.startUpCtrl = startUp.getKey();
        this.startUp = new Scene(startUp.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        this.userLoginCtrl = userLogin.getKey();
        this.userLogin = new Scene(userLogin.getValue());

        primary.setTitle("Talio");
        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        setStartUp();
        primary.show();
    }

    public void switchScene() {

    }


    /**
     * Switches the actual scene to the start-up scene.
     */
    public void setStartUp() {
        primaryStage.setScene(startUp);
    }

    /**
     * Switches the actual scene to the admin login scene.
     */
    public void setAdminLogin() {
        primaryStage.setScene(adminLogin);
    }

    /**
     * Switches the actual scene to the user login scene.
     */
    public void setUserLogin() {
        primaryStage.setScene(userLogin);
    }

    /**
     * Switches the actual scene to the workspace scene.
     */
    public void setWorkspace() {
        primaryStage.setScene(workspace);
    }

    public AdminLoginCtrl getAdminLoginCtrl() {
        return adminLoginCtrl;
    }

    public WorkspaceCtrl getWorkspaceCtrl() {
        return workspaceCtrl;
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     * @return the controller of the popUp
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
