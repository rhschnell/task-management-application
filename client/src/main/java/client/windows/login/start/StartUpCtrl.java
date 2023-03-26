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
package client.windows.login.start;

import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StartUpCtrl implements Initializable {
    private final StartUpService service;

    /**
     * Constructor for AdminLoginCtrl
     * @param service a startUpService
     */
    @Inject
    public StartUpCtrl(StartUpService service) {
        this.service = service;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * When called, it switches to the admin log in scene.
     */
    public void showAdminLogin() {
        service.showAdminLogin();
    }

    /**
     * When called, it switches to the user log in scene.
     */
    public void showUserLogIn() {
        service.showUserLogin();
    }
}
