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


public class MainCtrl {
    private Stage primaryStage;
    private Scene startUp;
    private Scene userLogin;
    private Scene adminLogin;
    private StartUpCtrl startUpCtrl;
    private UserLoginCtrl userLoginCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private WorkspaceCtrl workspaceCtrl;


    private HelperMethods hm;

    private Map<String, List<String>> serverToKeyListMap;

    /**
     * Initializes the Stages that needs to be switched within the app.
     *
     * @param primary       represents the primary stage
     * @param adminLogin    represents the pair of login scene, and its controller.
     */
    public void initialize(Stage primary,
                           Pair<StartUpCtrl, Parent> startUp,
                           Pair<UserLoginCtrl, Parent> userLogin,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           HelperMethods hm) {
        this.primaryStage = primary;

        this.startUpCtrl = startUp.getKey();
        this.startUp = new Scene(startUp.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.userLoginCtrl = userLogin.getKey();
        this.userLogin = new Scene(userLogin.getValue());

        this.serverToKeyListMap = new HashMap<String, List<String>>();

        primary.setTitle("Talio");
        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        //Temporarily disabled resizing because of full screen problems
        primary.setResizable(false);
        this.hm = hm;
        hm.setScenes(this.startUp, this.adminLogin, null, this.userLogin, null);
        hm.setPrimaryStage(primaryStage);
        hm.setScene(Scenes.STARTUP);
        hm.setCardFormat(new DataFormat("card"));
        hm.setMemMap(serverToKeyListMap);
        primary.show();
    }
}
