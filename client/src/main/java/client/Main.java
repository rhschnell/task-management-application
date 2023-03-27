/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.login.admin.AdminLoginCtrl;
import client.windows.login.start.StartUpCtrl;
import client.windows.login.user.UserLoginCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    private static final Injector INJECTOR = createInjector(new MainModules());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    /**
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        var startUp = FXML.load(StartUpCtrl.class, "client", "windows", "login", "start", "StartUp.fxml");
        var userLogin = FXML.load(UserLoginCtrl.class, "client", "windows", "login", "user", "UserLogin.fxml");
        var adminLogin = FXML
                        .load(AdminLoginCtrl.class, "client", "windows", "login", "admin", "AdminLogin.fxml");
        var workspace = FXML.load(WorkspaceCtrl.class, "client", "windows", "workspace", "Workspace.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        HelperMethods hm = INJECTOR.getInstance(HelperMethods.class);
        mainCtrl.initialize(primaryStage, startUp, userLogin, adminLogin, workspace, hm);
    }
}