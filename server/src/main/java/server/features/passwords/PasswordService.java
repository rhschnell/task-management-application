package server.features.passwords;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PasswordService {

    private String password;

    public PasswordService() {
        this.password = String.valueOf(((Integer)new Random().nextInt()).hashCode()).replace("-", "");
        System.out.println("The admin password is: " + password);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return this.password;
    }

    public boolean isPasswordCorrect(String enteredPassword) {
        return enteredPassword.equals(password);
    }
}