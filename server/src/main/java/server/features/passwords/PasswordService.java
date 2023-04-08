package server.features.passwords;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PasswordService {

    private String password;

    /**
     * Constructor for the PasswordService
     */
    public PasswordService() {
        this.password = String.valueOf(((Integer)new Random().nextInt()).hashCode()).replace("-", "");
        System.out.println("The admin password is: " + password);
    }

    /**
     * Setter for the password
     * @param password the password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Getter for the password
     * @return the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Checks the entered password against the server password
     * @param enteredPassword
     * @return true if password is correct, false otherwise
     */
    public boolean isPasswordCorrect(String enteredPassword) {
        return enteredPassword.equals(password);
    }
}