package client.scenes;

import client.MyFXML;
import client.scenes.MainScreens.WorkspaceCtrl;
import client.scenes.MainScreens.LoginCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {
    private Stage primaryStage;
    private Scene login;
    private Scene workspace;
    private MyFXML myFXML;
    private LoginCtrl loginCtrl;
    private WorkspaceCtrl workspaceCtrl;

    /**
     * Initializes the Stages that needs to be switched within the app.
     * @param primary represents the primary stage
     * @param login represents the pair of login scene, and it's controller.
     * @param myFXML used for reinitializing the workspace
     */
    public void initialize(Stage primary,
                           Pair<LoginCtrl,Parent> login,
                           MyFXML myFXML) {
        this.primaryStage = primary;

        this.loginCtrl = login.getKey();
        this.login = new Scene(login.getValue());

        this.myFXML = myFXML;

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
        var workspace = myFXML.load(WorkspaceCtrl.class, "client", "scenes","MainScreens","Workspace.fxml");
        primaryStage.setScene(new Scene(workspace.getValue()));
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     * @return
     */
    public Stage popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.setResizable(false);
        popUp.showAndWait();

        return popUp;
    }
}
