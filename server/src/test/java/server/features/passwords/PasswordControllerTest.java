package server.features.passwords;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class PasswordControllerTest {

    private PasswordController passwordController;

    @BeforeEach
    void before()
    {
        passwordController = new PasswordController(new PasswordService());
    }

    @Test
    void controllerConstructor()
    {
        assertNotNull(passwordController);
    }

    @Test
    void serviceConstructor()
    {
        PasswordService service = new PasswordService();
        assertNotNull(service);
    }

    @Test
    void getPassword()
    {
        PasswordService service = new PasswordService();
        assertEquals(String.class, service.getPassword().getClass());
    }

    @Test
    void setPassword()
    {
        PasswordService service = new PasswordService();
        service.setPassword("Test");
        assertEquals("Test", service.getPassword());
    }

    @Test
    void isPasswordCorrect()
    {
        PasswordService service = new PasswordService();
        assertTrue(service.isPasswordCorrect(service.getPassword()));
    }

    @Test
    void checkGoodPassword() {
        PasswordService service = new PasswordService();
        service.setPassword("Test");
        passwordController = new PasswordController(service);
        assertEquals(HttpStatus.OK, passwordController.checkPassword("Test").getStatusCode());
    }

    @Test
    void checkBadPassword()
    {
        PasswordService service = new PasswordService();
        service.setPassword("Test");
        assertEquals(HttpStatus.FORBIDDEN, passwordController.checkPassword("Wrong Password").getStatusCode());
    }
}