package client.scenes;

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
    private LoginCtrl loginCtrl;
    private WorkspaceCtrl workspaceCtrl;

    public void initialize(Stage primary, Pair<LoginCtrl, Parent> login,Pair<WorkspaceCtrl,Parent> workspace) {
        this.primaryStage = primary;

        this.loginCtrl = login.getKey();
        this.login = new Scene(login.getValue());

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        primary.setMinHeight(576);
        primary.setMinWidth(1024);
        setLogin();
        primary.show();
    }

    public void setLogin() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(login);
    }
    public void setBoardOverview() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(workspace);
    }

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
