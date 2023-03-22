package client.scenes.MainScreens;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class LoginCtrlTest {

    private ServerUtils server = new ServerUtils();
    private MainCtrl mainCtrl = new MainCtrl();

    private LoginCtrl sut;

    @BeforeEach
    void before() {
        LoginCtrl sut = new LoginCtrl(server, mainCtrl);
    }

    @Test
    void connect() {
//        sut.connect();
//        assertEquals("http://localhost:8080", sut.getServerAddress().getText());
    }

    @Test
    void showWorkspace() {
    }

    @Test
    void joinPopUp() {
    }
}