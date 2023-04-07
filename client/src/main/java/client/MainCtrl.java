package client;

import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.login.admin.AdminLoginCtrl;
import client.windows.login.start.StartUpCtrl;
import client.windows.login.user.UserLoginCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainCtrl {
    private Stage primaryStage;
    private Scene startUp;
    private Scene userLogin;
    private Scene adminLogin;
    private StartUpCtrl startUpCtrl;
    private UserLoginCtrl userLoginCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private WorkspaceCtrl workspaceCtrl;


    private HelperMethods helperMethods;

    private Map<String, Set<String>> serverToKeyListMap;

    /**
     * Initializes the Stages that needs to be switched within the app.
     *
     * @param primary       Represents the primary stage
     * @param startUp       Represents the pair of startup scene and its controller
     * @param userLogin     Represents the pair of login scene and its controller for the user
     * @param adminLogin    Represents the pair of login scene and its controller for the admin
     * @param helperMethods Instance of HelperMethods, providing easy access utilities
     */
    public void initialize(Stage primary,
                           Pair<StartUpCtrl, Parent> startUp,
                           Pair<UserLoginCtrl, Parent> userLogin,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           HelperMethods helperMethods) {
        this.primaryStage = primary;

        this.startUpCtrl = startUp.getKey();
        this.startUp = new Scene(startUp.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.userLoginCtrl = userLogin.getKey();
        this.userLogin = new Scene(userLogin.getValue());

        this.serverToKeyListMap = new HashMap<String, Set<String>>();

        primary.setTitle("Talio");
        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        //Temporarily disabled resizing because of full screen problems
        primary.setResizable(false);
        this.helperMethods = helperMethods;
        helperMethods.setScenes(this.startUp, this.adminLogin, null, this.userLogin, null);
        helperMethods.setPrimaryStage(primaryStage);
        helperMethods.setScene(Scenes.STARTUP);
        helperMethods.setCardFormat(new DataFormat("card"));
        helperMethods.setMemMap(serverToKeyListMap);
        primary.show();
    }
}
