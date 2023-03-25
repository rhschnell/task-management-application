package client.windows.login.user;

public class UserLoginModel {
    public void connect() {
        server.setServer(serverAddress.getText());
        if (server.pingServer()){
            showWorkspace();
        } else {
            showErrorMessage();
        }
    }
}
