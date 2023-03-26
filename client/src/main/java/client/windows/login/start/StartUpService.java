package client.windows.login.start;

import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;

public class StartUpService {
    private final HelperMethods hm;

    @Inject
    public StartUpService(HelperMethods hm) {
        this.hm = hm;
    }

    public void showAdminLogin() {
        hm.setScene(Scenes.ADMIN);
    }

    public void showUserLogin() {
        hm.setScene(Scenes.USER);
    }
}
