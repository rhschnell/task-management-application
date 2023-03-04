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
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    @Inject
    public LoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void openBoard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("BoardOverview.fxml"));
        mainCtrl.login = new Scene(root);
        mainCtrl.primaryStage.setScene(mainCtrl.login);
        mainCtrl.primaryStage.show();
    }
}